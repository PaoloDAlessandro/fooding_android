package it.itsar.fooding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ProductAddition extends AppCompatActivity {

    private final MyProperties myProperties = MyProperties.getInstance();
    private final Prodotto[] prodotti = myProperties.getProdotti();
    private ArrayList<Prodotto> userProdotti = myProperties.getUserProdotti();
    private List<Prodotto> prodottiFiltrati = null;
    private Prodotto selectedProduct;
    private Button confirmButton;
    private Button cancelButton;
    private EditText productStockInput;
    private Button decreaseStockInput;
    private Button increaseStockInput;
    private DatePickerDialog picker;
    private TextView productExpirationDateInput;
    private AutoCompleteTextView productNameAutoComplete;
    private int year;
    private int month;
    private int day;
    private DateTimeFormatter formatter;
    private LocalStorageManager localStorageManager = new LocalStorageManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_addition);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        }
        productExpirationDateInput = findViewById(R.id.productExpirationDateInput);
        productExpirationDateInput.setInputType(InputType.TYPE_NULL);
        productNameAutoComplete = findViewById(R.id.productNameInput);
        confirmButton = findViewById(R.id.productAdditionConfirm);
        cancelButton = findViewById(R.id.productAddictionCancel);
        productStockInput = findViewById(R.id.productStockInput);
        decreaseStockInput = findViewById(R.id.decreaseStockButton);
        increaseStockInput = findViewById(R.id.increaseStockButton);
        userProdotti = myProperties.getUserProdotti();
        selectedProduct = null;
        decreaseStockInput.setEnabled(false);
        confirmButton.setEnabled(false);
        productExpirationDateInput.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(ProductAddition.this,
                    (datePicker, year1, month1, day1) -> {
                        year = year1;
                        day = day1;
                        month = month1;
                        productExpirationDateInput.setText(day + "/" + (month + 1) + "/" + year);
                        checkInputsStatus();
                    }, year, month, day);
            picker.getDatePicker().setMinDate(new Date().getTime());
            picker.show();
        });

        List<Prodotto> prodottiDaFiltrare = Arrays.asList(prodotti);

        productStockInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputsStatus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        decreaseStockInput.setOnClickListener(view -> {
            productStockInput.setText(String.valueOf(Integer.parseInt(productStockInput.getText().toString()) - 1));
            if(Integer.parseInt(productStockInput.getText().toString()) == 1) {
                decreaseStockInput.setEnabled(false);
            }
        });

        increaseStockInput.setOnClickListener(view -> {
            if(Integer.parseInt(productStockInput.getText().toString()) == 1) {
                decreaseStockInput.setEnabled(true);
            }
            productStockInput.setText(String.valueOf(Integer.parseInt(productStockInput.getText().toString()) + 1));
        });

        productNameAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() >= 2) {
                    prodottiFiltrati = prodottiDaFiltrare.stream()
                            .filter(prodotto ->
                                    prodotto.getNome().toLowerCase().startsWith(productNameAutoComplete.getText().toString().toLowerCase()))
                            .collect(Collectors.toList());

                    if(prodottiFiltrati.size() != 0) {
                            selectedProduct = prodottiFiltrati.get(0);
                            checkInputsStatus();
                            ProductNameAutoCompleteAdapter productNameAutoCompleteAdapterAfterChange = new ProductNameAutoCompleteAdapter(getApplicationContext(), new ArrayList<>(prodottiFiltrati));
                            productNameAutoComplete.setAdapter(productNameAutoCompleteAdapterAfterChange);
                            productNameAutoCompleteAdapterAfterChange.notifyDataSetChanged();
                        }
                }
                }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        productNameAutoComplete.setOnItemClickListener((parent, arg1, position, arg3) -> {
            if (prodottiFiltrati.size() == 1) {
                selectedProduct = (Prodotto) parent.getItemAtPosition(0); //who wrote Java is an idiot
            }
            else {
                selectedProduct = (Prodotto) parent.getItemAtPosition(position);
            }
            checkInputsStatus();
        });

        confirmButton.setOnClickListener(view -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("d/M/yyyy");
                selectedProduct.setDateScadenza(new ArrayList<>(Arrays.asList(new ProductExpirationDate(Integer.parseInt(productStockInput.getText().toString()), LocalDate.parse(productExpirationDateInput.getText(), formatterDate)))));
            }
            checkProductInUserPantry();
            File file = new File(getFilesDir(), "storage.txt");
            try {
                localStorageManager.backupToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            goBack(Activity.RESULT_OK);
        });

        cancelButton.setOnClickListener((view) -> goBack(Activity.RESULT_CANCELED));
    }

    void goBack(int resultCode) {
        setResult(resultCode);
        finish();
    }

    void checkProductInUserPantry() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        selectedProduct.getDateScadenza().get(0).setExpirationDate(LocalDate.parse(productExpirationDateInput.getText(), formatter));
        }

        if(userProdotti.stream().anyMatch(prodotto -> prodotto.getNome().equals(selectedProduct.getNome()) &&
                prodotto.getMarca().equals(selectedProduct.getMarca()))) {

            int productIndex = userProdotti.indexOf(userProdotti.stream().filter(prodotto -> prodotto.getNome().equals(selectedProduct.getNome()) &&
                    prodotto.getMarca().equals(selectedProduct.getMarca())).collect(Collectors.toList()).get(0));

            if(myProperties.getUserProdotti().get(productIndex).getDateScadenza().stream().anyMatch(productExpirationDate -> productExpirationDate.getExpirationDate().equals(selectedProduct.getDateScadenza().get(0).getExpirationDate()))) {
                ArrayList<ProductExpirationDate> productExpirationDateOfProduct = myProperties.getUserProdotti().get(productIndex).getDateScadenza();
                int productExpirationDateIndex = productExpirationDateOfProduct.indexOf(
                        myProperties.getUserProdotti().get(productIndex).getDateScadenza()
                                .stream().
                                filter(
                                        productExpirationDate ->
                                                productExpirationDate.getExpirationDate()
                                                        .equals(selectedProduct.getDateScadenza().get(0).getExpirationDate())
                                )
                                .collect(Collectors.toList()).get(0));
                productExpirationDateOfProduct.get(productExpirationDateIndex).setAmount(selectedProduct.getDateScadenza().get(0).getAmount() + myProperties.getUserProdotti().get(productIndex).getDateScadenza().get(productExpirationDateIndex).getAmount());
            }
            else {
                myProperties.getUserProdotti().get(productIndex).addExpirationDate(new ProductExpirationDate(selectedProduct.getDateScadenza().get(0).getAmount(), selectedProduct.getDateScadenza().get(0).getExpirationDate()));
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                myProperties.getUserProdotti().add(new Prodotto(
                        selectedProduct.getNome(),
                        selectedProduct.getMarca(),
                        selectedProduct.getIngredienti(),
                        selectedProduct.getPeso(),
                        selectedProduct.getUnità(),
                        selectedProduct.getPreparazione(),
                        new ArrayList<>(Arrays.asList(
                                new ProductExpirationDate(Integer.parseInt(productStockInput.getText().toString()), LocalDate.parse(productExpirationDateInput.getText(), formatter))
                        )),
                        selectedProduct.getImage(),
                        selectedProduct.getColore(),
                        selectedProduct.getValoriNutrizionali(),
                        LocalDateTime.now()
                ));
            }
        }
    }

    boolean isNumeric(String text) {
        if (text == null) {
            return false;
        }

        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    void checkInputsStatus() {
        if(prodottiFiltrati != null) {
            boolean firstCondition = prodottiFiltrati.stream().anyMatch(prodotto -> prodotto.getNome().equals(productNameAutoComplete.getText().toString()));
            boolean secondCondition = productExpirationDateInput.getText().toString().length() > 0;
            boolean thirdCondition = isNumeric(productStockInput.getText().toString());

            if(!firstCondition) {
                productNameAutoComplete.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            }
            else {
                productNameAutoComplete.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.product_marca));
            }

            confirmButton.setEnabled(firstCondition && secondCondition && thirdCondition);
        }
    }

}
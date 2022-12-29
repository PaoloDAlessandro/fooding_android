package it.itsar.fooding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ProductAddition extends AppCompatActivity {

    private MyProperties myProperties = MyProperties.getInstance();
    private Prodotto[] prodotti = myProperties.getProdotti();
    private List<Prodotto> prodottiFiltrati = null;
    private Prodotto selectedProduct;
    private Button confirmButton;
    private Button cancelButton;
    private EditText productStockInput;
    private DatePickerDialog picker;
    private TextView productExpirationDateInput;
    private AutoCompleteTextView productNameAutoComplete;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_addition);

        productExpirationDateInput = findViewById(R.id.productExpirationDateInput);
        productExpirationDateInput.setInputType(InputType.TYPE_NULL);
        productNameAutoComplete = findViewById(R.id.productNameInput);
        confirmButton = findViewById(R.id.productAdditionConfirm);
        cancelButton = findViewById(R.id.productAddictionCancel);
        productStockInput = findViewById(R.id.productStockInput);

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
        ProductNameAutoCompleteAdapter productNameAutoCompleteAdapter = new ProductNameAutoCompleteAdapter(this, R.layout.autocomplete_product_name, Arrays.asList(prodotti));

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

        productNameAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prodottiFiltrati = prodottiDaFiltrare.stream()
                        .filter(prodotto ->
                                prodotto.getNome().contains(productNameAutoComplete.getText().toString()))
                        .collect(Collectors.toList());

                if(prodottiFiltrati.size() != 0) {
                        selectedProduct = prodottiFiltrati.get(0);
                        checkInputsStatus();
                        productNameAutoCompleteAdapter.setProdotti(prodottiFiltrati);
                        productNameAutoComplete.setAdapter(productNameAutoCompleteAdapter);
                    }
                }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        productNameAutoComplete.setAdapter(productNameAutoCompleteAdapter);
        productNameAutoComplete.setOnItemClickListener((parent, arg1, position, arg3) -> {
            selectedProduct = (Prodotto) parent.getItemAtPosition(position);
            checkInputsStatus();
        });

        confirmButton.setOnClickListener(view -> {
            selectedProduct.setGiacenza(Integer.parseInt(productStockInput.getText().toString()));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                selectedProduct.setDataScadenza(LocalDate.parse(productExpirationDateInput.getText(), formatter));
            }
            myProperties.addUserProduct(selectedProduct);
            finish();
        });

        cancelButton.setOnClickListener(view -> {
            finish();
        });
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
            Log.d("FILTER PRODUCTS: ", prodottiFiltrati.stream().filter(prodotto -> prodotto.getNome().equals(productNameAutoComplete.getText().toString())).collect(Collectors.toList()).toString());
            boolean firstCondition = prodottiFiltrati.stream().filter(prodotto -> prodotto.getNome().equals(productNameAutoComplete.getText().toString())).collect(Collectors.toList()).size() != 0;
            boolean secondCondition = productExpirationDateInput.getText().toString().length() > 0;
            boolean thirdCondition = isNumeric(productStockInput.getText().toString());
            if(firstCondition && secondCondition && thirdCondition) {
                confirmButton.setEnabled(true);
            }
            else {
                confirmButton.setEnabled(false);
            }
        }
    }

}
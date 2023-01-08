package it.itsar.fooding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ProductDetails extends AppCompatActivity {

    private TextView productName;
    private ImageView productImage;
    private TextView productBrand;
    private TextView productExpirationDate;
    private ConstraintLayout productExpirationDateLayout;
    private TextView productIngredienti;
    private TextView productStock;
    private Button increaseStock;
    private Button decreaseStock;
    private Prodotto prodotto;
    private TextView productEnergiaValue;
    private TextView productEnergiaAR;
    private TextView productEnergiaValue2;
    private TextView productGrassiValue;
    private TextView productGrassiAR;
    private TextView productGrassiSaturiAR;
    private TextView productGrassiSaturiValue;
    private TextView productCarboidratiValue;
    private TextView productCarboidratiAR;
    private TextView productZuccheriValue;
    private TextView productZuccheriAR;
    private TextView productFibreValue;
    private TextView productFibreAR;
    private TextView productProteineValue;
    private TextView productProteineAR;
    private TextView productSaleValue;
    private TextView productSaleAR;
    private ProductExpirationDate currentProductExpirationDate;
    private HashMap<ProductExpirationDate, TextView> expirationDateTextViewHashMap = new HashMap<>();


    private Intent resultIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        xmlElementsInit();
        int position = getIntent().getIntExtra("position", 0);
        resultIntent.putExtra("position", position);
        prodotto = (Prodotto) getIntent().getSerializableExtra("prodotto");
        xmlPopulation();
        setupResultIntent();

        increaseStock.setOnClickListener(view -> {
            currentProductExpirationDate.setAmount(currentProductExpirationDate.getAmount() + 1);
            Objects.requireNonNull(expirationDateTextViewHashMap.get(currentProductExpirationDate)).setText(String.valueOf(currentProductExpirationDate.getAmount()));
            setupResultIntent();
            checkStockStatus();
        });

        decreaseStock.setOnClickListener(view -> {
            currentProductExpirationDate.setAmount(currentProductExpirationDate.getAmount() - 1);
            Objects.requireNonNull(expirationDateTextViewHashMap.get(currentProductExpirationDate)).setText(String.valueOf(currentProductExpirationDate.getAmount()));
            setupResultIntent();
            checkStockStatus();
        });
    }

    void setupResultIntent() {
        productStock.setText("In stock: " + currentProductExpirationDate.getAmount());
        resultIntent.putExtra("prodotto", prodotto);
        setResult(Activity.RESULT_OK, resultIntent);
    }

    void checkStockStatus() {
        if(currentProductExpirationDate.getAmount() <= 0) {
            decreaseStock.setEnabled(false);
        }
        else if(!decreaseStock.isEnabled()) {
            decreaseStock.setEnabled(true);
        }
    }

    void xmlElementsInit() {
        productName = findViewById(R.id.productName);
        productImage = findViewById(R.id.productImage);
        productBrand = findViewById(R.id.productBrand);
        productExpirationDate = findViewById(R.id.productExpirationDate);
        productExpirationDateLayout = findViewById(R.id.productExpirationDateLayout);
        productIngredienti = findViewById(R.id.productIngredienti);
        productStock = findViewById(R.id.productStock);
        increaseStock = findViewById(R.id.increaseStockButton);
        decreaseStock = findViewById(R.id.decreaseStockButton);
        productEnergiaValue = findViewById(R.id.productEnergiaValue);
        productEnergiaValue2 = findViewById(R.id.productEnergiaValue2);
        productEnergiaAR = findViewById(R.id.productEnergiaAR);
        productGrassiValue = findViewById(R.id.productGrassiValue);
        productGrassiAR = findViewById(R.id.productGrassiAR);
        productGrassiSaturiValue = findViewById(R.id.productGrassiSaturiValue);
        productGrassiSaturiAR = findViewById(R.id.productGrassiSaturiAR);
        productCarboidratiValue = findViewById(R.id.productCarboidratiValue);
        productCarboidratiAR = findViewById(R.id.productCarboidratiAR);
        productZuccheriValue = findViewById(R.id.productZuccheriValue);
        productZuccheriAR = findViewById(R.id.productZuccheriAR);
        productFibreValue = findViewById(R.id.productFibreValue);
        productFibreAR = findViewById(R.id.productFibreAR);
        productProteineValue = findViewById(R.id.productProteineValue);
        productProteineAR = findViewById(R.id.productFibreAR);
        productSaleValue = findViewById(R.id.productSaleValue);
        productSaleAR = findViewById(R.id.productSaleAR);
    }

    void xmlPopulation() {
        DateTimeFormatter dateTimeFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        }
        currentProductExpirationDate = prodotto.getCloserExpirationdate();
        productName.setText(prodotto.getNome());
        productBrand.setText(prodotto.getMarca());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            productExpirationDate.setText(prodotto.getCloserExpirationdate().getExpirationDate().format(dateTimeFormatter));
        }
        productIngredienti.setText(prodotto.getIngredienti());
        productStock.setText("In stock: " + currentProductExpirationDate.getAmount());
        productImage.setImageResource(prodotto.getImage());
        productEnergiaValue.setText(prodotto.getValoriNutrizionali().getEnergia() + "kj");
        productEnergiaAR.setText(prodotto.getValoriNutrizionali().getEnergiaAR() + "%");
        productEnergiaValue2.setText(prodotto.getValoriNutrizionali().convertToKcal() + "kcal");
        productGrassiValue.setText(prodotto.getValoriNutrizionali().getGrassi() + "g");
        productGrassiAR.setText(prodotto.getValoriNutrizionali().getGrassiAR() + "%");
        productGrassiSaturiValue.setText(prodotto.getValoriNutrizionali().getGrassiSaturi() + "g");
        productGrassiSaturiAR.setText(prodotto.getValoriNutrizionali().getGrassiSaturiAR() + "%");
        productCarboidratiValue.setText(prodotto.getValoriNutrizionali().getCarboidrati() + "g");
        productCarboidratiAR.setText(prodotto.getValoriNutrizionali().getCarboidratiAR() + "%");
        productZuccheriValue.setText(prodotto.getValoriNutrizionali().getZuccheri() + "g");
        productZuccheriAR.setText(prodotto.getValoriNutrizionali().getZuccheriAR() + "%");
        productFibreValue.setText(prodotto.getValoriNutrizionali().getFibre() + "g");
        productFibreAR.setText(prodotto.getValoriNutrizionali().getFibreAR() + "%");
        productProteineValue.setText(prodotto.getValoriNutrizionali().getProteine() + "g");
        productProteineAR.setText(prodotto.getValoriNutrizionali().getProteineAR() + "%");
        productSaleValue.setText(prodotto.getValoriNutrizionali().getSale() + "g");
        productSaleAR.setText(prodotto.getValoriNutrizionali().getSaleAR() + "%");
        ArrayList<TextView> expirationDates = new ArrayList<>();
        ArrayList<TextView> amountOfProduct = new ArrayList<>();
        for (ProductExpirationDate productExpirationDate:prodotto.getDateScadenza()) {
            TextView textViewExpirationDate = new TextView(getApplicationContext());
            int textId = ViewCompat.generateViewId();
            textViewExpirationDate.setId(textId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textViewExpirationDate.setText(productExpirationDate.getExpirationDate().format(dateTimeFormatter));
            }
            textViewExpirationDate.setPadding(0, 0, 550, 20);
            expirationDates.add(textViewExpirationDate);
            productExpirationDateLayout.addView(textViewExpirationDate);
            textViewExpirationDate.setOnClickListener(view -> {
                currentProductExpirationDate = productExpirationDate;
                productStock.setText("In stock: " + currentProductExpirationDate.getAmount());
            });
            TextView textViewAmountOfProduct = new TextView(getApplicationContext());
            textId = ViewCompat.generateViewId();
            textViewAmountOfProduct.setId(textId);
            textViewAmountOfProduct.setText(String.valueOf(productExpirationDate.getAmount()));
            textViewAmountOfProduct.setPadding(0, 0, 0, 20);
            amountOfProduct.add(textViewAmountOfProduct);
            expirationDateTextViewHashMap.put(productExpirationDate, textViewAmountOfProduct);
            productExpirationDateLayout.addView(textViewAmountOfProduct);
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(productExpirationDateLayout);
        constraintSet.connect(expirationDates.get(0).getId(), ConstraintSet.TOP, R.id.expirationDateLabel, ConstraintSet.BOTTOM);
        int counter = 0;
        for(TextView textView : expirationDates.subList(1, expirationDates.size())) {
            constraintSet.connect(textView.getId(), ConstraintSet.TOP, expirationDates.get(counter).getId(), ConstraintSet.BOTTOM);
            counter++;
        }

        constraintSet.connect(amountOfProduct.get(0).getId(), ConstraintSet.TOP, R.id.quantitàLabel, ConstraintSet.BOTTOM);
        constraintSet.connect(amountOfProduct.get(0).getId(), ConstraintSet.START, expirationDates.get(0).getId(), ConstraintSet.END);

        counter = 0;

        for(TextView textView : amountOfProduct.subList(1, amountOfProduct.size())) {
            constraintSet.connect(textView.getId(), ConstraintSet.TOP, amountOfProduct.get(counter).getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(textView.getId(), ConstraintSet.START, expirationDates.get(counter + 1).getId(), ConstraintSet.END);
            counter++;
        }
        constraintSet.applyTo(productExpirationDateLayout);
    }
}
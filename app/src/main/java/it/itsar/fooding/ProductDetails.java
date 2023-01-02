package it.itsar.fooding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.intellij.lang.annotations.Identifier;
import org.w3c.dom.Text;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProductDetails extends AppCompatActivity {

    private TextView productName;
    private ImageView productImage;
    private TextView productBrand;
    private TextView productExpirationDate;
    //private ConstraintLayout productExpirationDateLayout;
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
            //prodotto.setGiacenza(prodotto.getGiacenza() + 1);
            setupResultIntent();
            checkStockStatus();
        });

        decreaseStock.setOnClickListener(view -> {
            //prodotto.setGiacenza(prodotto.getGiacenza() - 1);
            setupResultIntent();
            checkStockStatus();
        });
    }

    void setupResultIntent() {
        productStock.setText("In stock: " + prodotto.getAmountOfUnits());
        resultIntent.putExtra("prodotto", prodotto);
        setResult(Activity.RESULT_OK, resultIntent);
    }

    void checkStockStatus() {
        /*if(prodotto.getGiacenza() <= 0) {
            decreaseStock.setEnabled(false);
        }
        else if(!decreaseStock.isEnabled()) {
            decreaseStock.setEnabled(true);
        }

         */
    }

    void xmlElementsInit() {
        productName = findViewById(R.id.productName);
        productImage = findViewById(R.id.productImage);
        productBrand = findViewById(R.id.productBrand);
        productExpirationDate = findViewById(R.id.productExpirationDate);
        //productExpirationDateLayout = findViewById(R.id.productExpirationDateLayout);
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
        productName.setText(prodotto.getNome());
        productBrand.setText(prodotto.getMarca());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            productExpirationDate.setText(prodotto.getCloserExpirationdate().getExpirationDate().format(dateTimeFormatter));
        }
        productIngredienti.setText(prodotto.getIngredienti());
        productStock.setText("In stock: " + prodotto.getAmountOfUnits());
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
            /*
        ArrayList<Integer> expirationDatesId = new ArrayList<>();
        int counter = 0;
        for (ProductExpirationDate productExpirationDate:prodotto.getDateScadenza()) {
            ConstraintSet set = new ConstraintSet();
            set.clone(productExpirationDateLayout);
            TextView textView = new TextView(getApplicationContext());
            int textId = ViewCompat.generateViewId();
            textView.setId(textId);
            expirationDatesId.add(textView.getId());
            textView.setText(productExpirationDate.getExpirationDate().toString());
            productExpirationDateLayout.addView(textView);
            if(expirationDatesId.size() > 1) {
                set.connect(textView.getId(), ConstraintSet.TOP, productExpirationDateLayout.getId(), ConstraintSet.BOTTOM);
            } else {
                set.connect(textView.getId(), ConstraintSet.TOP, productExpirationDateLayout.getId(), ConstraintSet.TOP);
            }

            set.connect(productExpirationDateLayout.getId(), ConstraintSet.TOP, productExpirationDateLayout.getId(), ConstraintSet.TOP);
            set.applyTo(productExpirationDateLayout);

            counter++;
        }
             */

    }
}
package it.itsar.fooding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProductDetails extends AppCompatActivity {

    private TextView productName;
    private ImageView productImage;
    private TextView productBrand;
    private TextView productIngredienti;
    private TextView productStock;
    private Button increaseStock;
    private Button decreaseStock;

    private Prodotto prodotto;

    private Intent resultIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setTitle("My pantry");
        productName = findViewById(R.id.productName);
        productImage = findViewById(R.id.productImage);
        productBrand = findViewById(R.id.productBrand);
        productIngredienti = findViewById(R.id.productIngredienti);
        productStock = findViewById(R.id.productStock);
        increaseStock = findViewById(R.id.increaseStockButton);
        decreaseStock = findViewById(R.id.decreaseStockButton);


        int position = getIntent().getIntExtra("position", 0);
        prodotto = (Prodotto) getIntent().getSerializableExtra("prodotto");
        resultIntent.putExtra("prodotto", prodotto);
        resultIntent.putExtra("position", position);
        setResult(Activity.RESULT_CANCELED, resultIntent);
        productName.setText(prodotto.getNome());
        productBrand.setText(prodotto.getMarca());
        productIngredienti.setText(prodotto.getIngredienti());
        productStock.setText("In stock: " + prodotto.getGiacenza());
        productImage.setImageResource(prodotto.getImage());

        increaseStock.setOnClickListener(view -> {
            prodotto.setGiacenza(prodotto.getGiacenza() + 1);
            productStock.setText("In stock: " + prodotto.getGiacenza());
            resultIntent.putExtra("prodotto", prodotto);
            setResult(Activity.RESULT_OK, resultIntent);

            checkStockStatus();
        });

        decreaseStock.setOnClickListener(view -> {
            prodotto.setGiacenza(prodotto.getGiacenza() - 1);
            productStock.setText("In stock: " + prodotto.getGiacenza());
            resultIntent.putExtra("prodotto", prodotto);
            setResult(Activity.RESULT_OK, resultIntent);

            checkStockStatus();
        });
    }

    void checkStockStatus() {
        if(prodotto.getGiacenza() <= 0) {
            decreaseStock.setEnabled(false);
        }
        else if(!decreaseStock.isEnabled()) {
            decreaseStock.setEnabled(true);
        }
    }
}
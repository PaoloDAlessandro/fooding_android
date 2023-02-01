package it.itsar.fooding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RicettaDetails extends AppCompatActivity {

    private RecyclerView ingredientiList;
    private IngredientiRicettaAdapter ingredientiRicettaAdapter;
    private Ricetta ricetta;
    private TextView ricettaNome;
    private TextView ricettaAutore;
    private TextView ricettaDurata;
    private TextView ricettaDifficolta;
    private TextView ricettaKcal;
    private ImageView ricettaImage;
    private Button preparaButton;
    private MyProperties myProperties = MyProperties.getInstance();
    private ArrayList<Prodotto> userProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricetta_details);

        ricettaNome = findViewById(R.id.ricettaName);
        ricettaAutore = findViewById(R.id.ricettaAutore);
        ricettaDifficolta = findViewById(R.id.difficoltaText);
        ricettaDurata = findViewById(R.id.durataText);
        ricettaKcal = findViewById(R.id.kcalText);
        preparaButton = findViewById(R.id.preparaButton);

        userProduct = myProperties.getUserProdotti();

        ingredientiList = findViewById(R.id.ingredientiList);
        ricetta = (Ricetta) getIntent().getSerializableExtra("ricetta");

        initXmlElements();
        populateXmlElements();

        setPreparaButtonEventListener();
        preparaButtonStatusManager();


        ingredientiRicettaAdapter = new IngredientiRicettaAdapter(ricetta.getIngredienti());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ingredientiList.setLayoutManager(layoutManager);
        ingredientiList.setAdapter(ingredientiRicettaAdapter);
    }

    void initXmlElements() {
        ricettaNome = findViewById(R.id.ricettaName);
        ricettaAutore = findViewById(R.id.ricettaAutore);
        ricettaDifficolta = findViewById(R.id.difficoltaText);
        ricettaDurata = findViewById(R.id.durataText);
        ricettaKcal = findViewById(R.id.kcalText);
        ricettaImage = findViewById(R.id.ricettaImage);
    }

    void populateXmlElements() {
        downloadImage(ricetta.getImage());
        ricettaNome.setText(ricetta.getNome());
        ricettaAutore.setText("by " + ricetta.getAutore());
        ricettaDurata.setText(ricetta.getTempoPreparazione() + "min.");
        switch (ricetta.getDifficolta()) {
            case FACILE:
                ricettaDifficolta.setText("facile");
                break;
            case MEDIA:
                ricettaDifficolta.setText("media");
                break;
            case DIFFICILE:
                ricettaDifficolta.setText("difficile");
        }
        ricettaKcal.setText(ricetta.getKcal() + " kcal");
    }

    boolean checkIngrentiInUserProducts() {
        ArrayList<Ingrediente> prova = new ArrayList<>();

        for (Prodotto prodottoUtente: userProduct) {
            for (Ingrediente ingrediente: ricetta.getIngredienti()) {
                if(ingrediente.getProdotto().getNome().equals(prodottoUtente.getNome())) {
                    prova.add(ingrediente);
                }
            }
        }
        return prova.containsAll(ricetta.getIngredienti());
    }

    void preparaButtonStatusManager() {
        if(checkIngrentiInUserProducts()) {
            preparaButton.setEnabled(true);
        }
        else {
            preparaButton.setEnabled(false);
        }
    }

    void setPreparaButtonEventListener() {
        preparaButton.setOnClickListener(view -> {

        });
    }

    private void downloadImage(String imageUri) {
        Picasso.get()
                .load(imageUri)
                .into(ricettaImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess(){
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
    }

}
package it.itsar.fooding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    private final MyProperties myProperties = MyProperties.getInstance();
    private LocalStorageManager localStorageManager = new LocalStorageManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomMenu);

        myProperties.setProdotti(new Prodotto[]{
                new Prodotto(
                        "Crema carciofi",
                        "Knorr",
                        "Farina di FRUMENTO, LATTE scremato, fecola di patate, PANNA, sale iodato, farina di mais*, aromi, carciofi 3,1%, olio di mais, estratto per brodo, zucchero, estratto di lievito, succo di limone, spinaci, sale, prezzemolo 0,2%, spezie (pepe nero*, aglio, radice di levistico). Può contenere uova, soia, sedaano e senape. *Da agricoltura sostenibile",
                        250,
                        "g",
                        3,
                        new ArrayList<>(),
                        R.drawable.crema_carciofi,
                        ContextCompat.getColor(getApplicationContext(), R.color.crema_carciofi),
                        new ValoriNutrizionali(724.00, 9, 11.0, 16, 6.3, 32, 17.0, 7, 1.3, 1, 1.3, 0, 2.5, 5, 0, 0)
                ),
                new Prodotto(
                        "Ragù Contadino",
                        "Barilla",
                        "Carote 4% Rosmarino Oli vegetali (oliva, girasole) Aglio Aromi Sale Zucchero Amido di mais Cipolle Vino rosso 6% Concentrato di pomodoro 16,5% Polpa di pomodoro 18% Acqua Carne suina 25%",
                        400,
                        "g",
                        0,
                        new ArrayList<>(),
                        R.drawable.ragu_contadino,
                        ContextCompat.getColor(getApplicationContext(), R.color.ragù_contadino),
                        new ValoriNutrizionali(385, 5, 4.5, 6, 1.2, 6, 6.8, 3, 4.0, 4, 1.8, 0, 5.1, 10, 1.0, 17)),
                new Prodotto(
                        "Leerdammer",
                        "Leerdammer",
                        "Latte, sale, fermenti lattici, caglio microbico.",
                        350,
                        "g",
                        0,
                        new ArrayList<>(),
                        R.drawable.leerdammer,
                        ContextCompat.getColor(getApplicationContext(), R.color.leerdammer),
                        new ValoriNutrizionali(1536, 18, 28.0, 40, 0, 0, 0.1, 0, 0, 0, 0, 0, 28, 56, 0, 0)),
                new Prodotto(
                        "Rosette",
                        "Schär",
                     "amido di mais , acqua , pasta madre (farina di riso, acqua) 14% , fibra vegetale (psillio) , farina di miglio , olio di girasole 3,8% , farina di riso , proteine di soia , amido di riso , addensante: idrossipropilmetilcellulosa ; sciroppo di riso , sale , lievito , zucchero . Può contenere tracce di nocciole, uova e di senape. SENZA FRUMENTO.",
                        500,
                        "g",
                        8,
                        new ArrayList<>(),
                        R.drawable.rosette,
                        ContextCompat.getColor(getApplicationContext(), R.color.rosette),
                        new ValoriNutrizionali(1025, 12.3, 3.6, 5.1, 0.5, 2.5, 47, 17.4, 0.8, 0.9, 5.5, 22, 3.5, 7, 1.3, 21.7)
                ),
                new Prodotto(
                        "Penne rigate N.73",
                        "Barilla",
                    "Semola di grano duro e acqua",
                        500,
                        "g",
                  11,
                             new ArrayList<>(),
                             R.drawable.penne_rigate_barilla,
                             ContextCompat.getColor(getApplicationContext(), R.color.penne_rigate_barilla),
                             new ValoriNutrizionali(1502, 18, 2, 3, 0.5, 3, 71.2, 27, 3.5, 4, 3, 0 , 12.5, 25, 0.03, 1)
                ),
                new Prodotto(
                        "Mozzarella",
                        "Granarolo",
                        "Latte pastorizzato, sale, caglio microbico, correttore d'acidità: acido citrico (E330) Prodotto con latte fresco pastorizzato di Alta Qualità, in conformità al D.M. 185/91.",
                        300,
                        "g",
                        0,
                        new ArrayList<>(),
                        R.drawable.mozzarella_granarolo,
                        ContextCompat.getColor(getApplicationContext(), R.color.mozzarella_granarolo),
                        new ValoriNutrizionali(904, 11, 16, 23, 11, 55, 1, 0, 1, 1, 0, 0, 17, 34, 0.7, 12)
                ),
                new Prodotto(
                        "Fusilli N.98",
                        "Barilla",
                        "Semola di grano duro e acqua",
                        500,
                        "g",
                        11,
                        new ArrayList<>(),
                        R.drawable.fusilli_n98_barilla,
                        ContextCompat.getColor(getApplicationContext(), R.color.fusilli_n98_barilla),
                        new ValoriNutrizionali(1502, 18, 2, 3, 0.5, 3, 71.2, 27, 3.5, 4, 3, 0 , 12.5, 25, 0.03, 1)
                ),
                new Prodotto(
                        "Mozzarella",
                        "Santa Lucia",
                        "Latte, sale, correttore di acidità: acido citrico*",
                        125,
                        "g",
                        0,
                        new ArrayList<>(),
                        R.drawable.mozzarella_santa_lucia,
                        ContextCompat.getColor(getApplicationContext(), R.color.mozzarella_santa_lucia),
                        new ValoriNutrizionali(996, 12, 18, 26, 12.5, 63, 2, 1, 1, 1, 0, 0, 17, 34, 1.75, 29)
                        ),
                new Prodotto(
                        "Olio extra vergine di oliva",
                        "Dante",
                        "Olio extra vergine di oliva",
                        1,
                        "L",
                        0,
                        new ArrayList<>(),
                        R.drawable.olio_extra_vergine_oliva_dante,
                        ContextCompat.getColor(getApplicationContext(), R.color.olio_extra_vergine_oliva_dante),
                        new ValoriNutrizionali(3439, 41, 91.3, 130, 13, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                ),
                new Prodotto(
                        "Tagliatelle n. 203",
                        "De Cecco",
                        "Pasta di semola di grano duro. Può contenere uova, soia e senape",
                        500,
                        "g",
                        5,
                        new ArrayList<>(),
                        R.drawable.tagliatelle_n203_de_cecco,
                        ContextCompat.getColor(getApplicationContext(), R.color.tagliatelle_n203_de_cecco),
                        new ValoriNutrizionali(1444.45, 17.3, 2.8, 4, 0.6, 3, 61, 22.6, 2.4, 2.7, 8, 32, 15, 30, 0.01, 0.2)
                ),
                new Prodotto(
                        "Riso Venere",
                        "Riso Scotti",
                        "Riso medio nero venere integrale parboiled",
                        500,
                        "g",
                        12,
                        new ArrayList<>(),
                        R.drawable.riso_venere_scotti,
                        ContextCompat.getColor(getApplicationContext(), R.color.riso_venere_scotti),
                        new ValoriNutrizionali(1448, 17, 3.7, 5, 1, 5, 72, 28, 0.1, 0, 2.5, 0, 8.1, 16, 0, 0)
                ),
                new Prodotto(
                        "Mozzarella fior di latte",
                        "Vallelata",
                        "Latte vaccino, fermenti lattici vivi, sale",
                        405,
                        "g",
                        0,
                        new ArrayList<>(),
                        R.drawable.mozzarella_fior_di_latte_vallelata,
                        ContextCompat.getColor(getApplicationContext(), R.color.mozzarella_fiord_di_latte_vallelata),
                        new ValoriNutrizionali(904, 11, 16, 23, 11, 55, 1, 0, 0.1, 0, 0, 0, 17, 34, 0.8, 13)
                ),
                new Prodotto(
                        "Uova guscio bianco",
                        "Le naturelle",
                     "uova",
                         300,
                        "g",
                   0,
                              new ArrayList<>(),
                              R.drawable.uova_le_naturelle,
                              ContextCompat.getColor(getApplicationContext(), R.color.uova_le_naturelle),
                              new ValoriNutrizionali(535, 6.4, 8.7, 12, 4, 20, 0.5, 0, 0.5, 0, 0, 0, 12, 24, 0.3, 5.7)
                )
        });
        setLocalStorageManager();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeButton:
                    configFragmentManager(Home.class);
                    break;

                case R.id.pantryButton:
                    configFragmentManager(Pantry.class);
                    break;
            }

            return true;
        });
    }

    private void setLocalStorageManager() {
        try {
            localStorageManager.backupFromFile(getFilesDir() + "/temp.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(myProperties.getUserProdotti().size() == 0) {
            try {
                localStorageManager.backupToFile(new File(getFilesDir() + "/temp.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void configFragmentManager(Class fragmentClass) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
}
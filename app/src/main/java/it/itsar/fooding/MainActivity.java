package it.itsar.fooding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    private final MyProperties myProperties = MyProperties.getInstance();
    private LocalStorageManager localStorageManager = new LocalStorageManager();
    private final AuthStorageManager authStorageManager = new AuthStorageManager();

    public static boolean isLogged;

    User userFromFile;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomMenu);
        userFromFile = authStorageManager.backupFromFile(getFilesDir() + AuthStorageManager.AUTH_FILE_NAME);
        checkUserIsLogged();
        getUserProduct();

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
                        new ValoriNutrizionali(724.00, 9, 11.0, 16, 6.3, 32, 17.0, 7, 1.3, 1, 1.3, 0, 2.5, 5, 0, 0),
                        null
                ),
                new Prodotto(
                        "Sale grosso",
                        "Carrefour",
                        "Sale grosso",
                        1,
                        "kg",
                        0,
                        new ArrayList<>(),
                        R.drawable.sale_grosso_carrefour,
                        ContextCompat.getColor(getApplicationContext(), R.color.sale_grosso_carrefour),
                        new ValoriNutrizionali(0, 0,0, 0,0,0,0,0,0,0, 0,0, 0, 0, 96.9, 1615),
                        null
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
                        new ValoriNutrizionali(385, 5, 4.5, 6, 1.2, 6, 6.8, 3, 4.0, 4, 1.8, 0, 5.1, 10, 1.0, 17),
                        null
                ),
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
                        new ValoriNutrizionali(1536, 18, 28.0, 40, 0, 0, 0.1, 0, 0, 0, 0, 0, 28, 56, 0, 0),
                        null
                ),
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
                        new ValoriNutrizionali(1025, 12.3, 3.6, 5.1, 0.5, 2.5, 47, 17.4, 0.8, 0.9, 5.5, 22, 3.5, 7, 1.3, 21.7),
                        null
                ),
                new Prodotto(
                        "Pepe nero",
                        "Cannamela",
                        "Pepe nero",
                        50,
                        "g",
                        0,
                        new ArrayList<>(),
                        R.drawable.pepe_nero_cannamela,
                        ContextCompat.getColor(getApplicationContext(), R.color.pepe_nero_cannamela),
                        new ValoriNutrizionali(11, 0, 0.03, 0, 0.01, 0, 0.65, 0, 0.01, 0, 0.03, 0.0, 0.11, 0, 0, 0)
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
                             new ValoriNutrizionali(1502, 18, 2, 3, 0.5, 3, 71.2, 27, 3.5, 4, 3, 0 , 12.5, 25, 0.03, 1),
                        null
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
                        new ValoriNutrizionali(904, 11, 16, 23, 11, 55, 1, 0, 1, 1, 0, 0, 17, 34, 0.7, 12),
                        null
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
                        new ValoriNutrizionali(1502, 18, 2, 3, 0.5, 3, 71.2, 27, 3.5, 4, 3, 0 , 12.5, 25, 0.03, 1),
                        null
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
                        new ValoriNutrizionali(996, 12, 18, 26, 12.5, 63, 2, 1, 1, 1, 0, 0, 17, 34, 1.75, 29),
                        null
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
                        new ValoriNutrizionali(3439, 41, 91.3, 130, 13, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                        null
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
                        new ValoriNutrizionali(1444.45, 17.3, 2.8, 4, 0.6, 3, 61, 22.6, 2.4, 2.7, 8, 32, 15, 30, 0.01, 0.2),
                        null
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
                        new ValoriNutrizionali(1448, 17, 3.7, 5, 1, 5, 72, 28, 0.1, 0, 2.5, 0, 8.1, 16, 0, 0),
                        null
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
                        new ValoriNutrizionali(904, 11, 16, 23, 11, 55, 1, 0, 0.1, 0, 0, 0, 17, 34, 0.8, 13),
                        null
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
                              new ValoriNutrizionali(535, 6.4, 8.7, 12, 4, 20, 0.5, 0, 0.5, 0, 0, 0, 12, 24, 0.3, 5.7),
                        null
                ),
                new Prodotto(
                        "Latte fieno",
                        "Mila",
                        "Latte fieno biologico dell'Alto Adige ",
                        1,
                        "L",
                        0,
                        new ArrayList<>(),
                        R.drawable.latte_fieno_mila,
                        ContextCompat.getColor(getApplicationContext(), R.color.latte_fieno_mila),
                        new ValoriNutrizionali(280, 3, 3.6, 5, 2.5, 13, 5.1, 2, 4.9, 5, 0, 0, 4.5, 7, 0.1, 2),
                        null
                )
        });
        setLocalStorageManager();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeButton:
                    if (isLogged) {
                        configFragmentManager(Home.class);
                    } else {
                        configFragmentManager(Login.class);
                    }
                    break;

                case R.id.pantryButton:
                    if (isLogged) {
                        configFragmentManager(Pantry.class);
                    } else {
                        configFragmentManager(Login.class);
                        Toast.makeText(this, "wow", Toast.LENGTH_SHORT);
                    }
                    break;

                case R.id.userButton:
                    if (isLogged) {
                        configFragmentManager(Account.class);
                    } else {
                        configFragmentManager(Login.class);
                    }
                    break;
            }

            return true;
        });
    }

    private void setLocalStorageManager() {
        try {
            localStorageManager.backupFromFile(getFilesDir() + "/storage.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(myProperties.getUserProdotti().size() == 0) {
            try {
                localStorageManager.backupToFile(new File(getFilesDir() + "/storage.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void configFragmentManager(Class fragmentClass) {
        if (fragmentClass == Account.class) {
            findViewById(R.id.logoutIcon).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.logoutIcon).setVisibility(View.INVISIBLE);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserIsLogged();
    }

    void checkUserIsLogged() {
        String usernameFromFile = userFromFile.getUsername();
        String passwordFromFile = userFromFile.getPassword();
            userCollection.whereEqualTo("username", usernameFromFile)
                    .whereEqualTo("password", passwordFromFile)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
                                isLogged = false;
                                configFragmentManager(Login.class);
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                isLogged = true;
                                Log.d("Result: ", document.getId() + " ==> " + document.getData());
                            }
                        } else {
                            Log.d("Error:", "Error getting documents: " + task.getException());
                        }
                    }
                });
    }

    void getUserProduct() {
        userCollection.whereEqualTo("username", userFromFile.getUsername())
                .whereEqualTo("password", userFromFile.getPassword())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CollectionReference userProducts = db.collection("user").document(document.getId()).collection("products");
                                userProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().size() == 0) {
                                                Log.d("RESULT: ", "User's pantry of: " + userFromFile.getUsername() + " is empty");
                                            }
                                            for (QueryDocumentSnapshot productDocument : task.getResult()) {
                                                ArrayList<Long> valoriNutrizionaliValues = (ArrayList<Long>) productDocument.getData().get("valoriNutrizionali");
                                                Log.d("valori nutrizionali: ", productDocument.getData().get("valoriNutrizionali").toString());

                                                Long peso = (Long) productDocument.getData().get("peso");
                                                Long preparazione = (Long) productDocument.getData().get("preparazione");
//                                              Long colore = (Long) productDocument.getData().get("colore");


                                                Prodotto tempProduct = new Prodotto(
                                                        (String) productDocument.getData().get("nome"),
                                                        (String) productDocument.getData().get("marca"),
                                                        (String) productDocument.getData().get("ingredienti"),
                                                        Integer.valueOf(peso.toString()),
                                                        (String) productDocument.getData().get("unità"),
                                                        Integer.valueOf(preparazione.toString()),
                                                        new ArrayList<>(),
                                                        1332,
                                                        4342,
                                                        new ValoriNutrizionali(
                                                            ((Number) valoriNutrizionaliValues.get(0)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(1)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(2)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(3)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(4)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(5)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(6)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(7)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(8)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(9)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(10)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(11)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(12)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(13)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(14)).doubleValue(),
                                                            ((Number) valoriNutrizionaliValues.get(15)).doubleValue()),
                                                        null
                                                );

                                                Log.d("Product: ", productDocument.getId() + " ==> " + productDocument.getData());
                                                Log.d("Product cloned: ", tempProduct.toString());
                                            }
                                        }
                                    }
                                });
                                Log.d("Result: ", document.getId() + " ==> " + document.getData());
                            }
                        }
                    }
                });
    }

}
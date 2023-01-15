package it.itsar.fooding;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Home extends Fragment {

    private RecyclerView ultimeAggiunteProdotti;
    private RecyclerView ricetteConsigliate;
    private final MyProperties myProperties = MyProperties.getInstance();
    private ArrayList<Prodotto> userProducts;
    private ArrayList<Ricetta> ricette;
    private LocalStorageManager localStorageManager = new LocalStorageManager();
    private UltimeAggiunteAdapter ultimeAggiunteAdapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("user");
    private CollectionReference productCollection = db.collection("product");

    private User userFromFile;

    private TextView welcomeMessage;

    private final AuthStorageManager authStorageManager = new AuthStorageManager();


    private ActivityResultLauncher<Intent> ricettaDetailsActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

            }
    );

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                switch (result.getResultCode()) {
                    case Activity.RESULT_OK:
                        updateUserProducts(result);
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d("Edit: ", "NO");
                        break;
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ultimeAggiunteProdotti = view.findViewById(R.id.ultimeAggiunteProdotti);
        ultimeAggiunteAdapter = new UltimeAggiunteAdapter(null, getContext(), activityLauncher);
        ultimeAggiunteProdotti.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ricetteConsigliate = view.findViewById(R.id.ricetteConsigliate);
        ricetteConsigliate.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        userFromFile = authStorageManager.backupFromFile(getActivity().getFilesDir() + AuthStorageManager.AUTH_FILE_NAME);
        getUltimeAggiunte();
        configUsernameText(userFromFile.getUsername());
        getProdotti();

        /*
        ricette = new ArrayList<>(Arrays.asList(
                new Ricetta("Tagliatelle al ragù", R.drawable.tagliatelle_al_ragu, "GialloZafferano", new ArrayList<>(Arrays.asList(new Ingrediente(80, myProperties.getProdotti()[11]), new Ingrediente(60, myProperties.getProdotti()[2]), new Ingrediente(10, myProperties.getProdotti()[10]), new Ingrediente(5, myProperties.getProdotti()[5]))), 11, 324, Ricetta.Difficolta.FACILE),
                new Ricetta("Crema carciofi", R.drawable.crema_carciofi_ricetta, "Knorr", new ArrayList<>(Arrays.asList(new Ingrediente(88, myProperties.getProdotti()[0]))), 8, 154, Ricetta.Difficolta.FACILE),
                new Ricetta("Omelette", R.drawable.omelette_ricetta, "BurroFuso", new ArrayList<>(Arrays.asList(new Ingrediente(120, myProperties.getProdotti()[14]), new Ingrediente(15, myProperties.getProdotti()[10]), new Ingrediente(30, myProperties.getProdotti()[15]))), 5, 287, Ricetta.Difficolta.FACILE)
        ));
         */

        ricette = new ArrayList<>(Arrays.asList(
                new Ricetta("Tagliatelle al ragù", R.drawable.tagliatelle_al_ragu, "GialloZafferano", null, 11, 324, Ricetta.Difficolta.FACILE)
        ));

        RicetteConsigliateAdapter ricetteConsigliateAdapter = new RicetteConsigliateAdapter(ricette, getContext(), ricettaDetailsActivityLauncher);
        ricetteConsigliate.setAdapter(ricetteConsigliateAdapter);

    }

    void updateUserProducts(ActivityResult result) {
        Intent intent = result.getData();
        assert intent != null;
        int position = intent.getIntExtra("position", -1);
        Prodotto prodotto = (Prodotto) intent.getSerializableExtra("prodotto");
        prodotto.checkEmptyExpirationDate();
        userProducts.get(position).setDateScadenza(prodotto.getDateScadenza());
        myProperties.removeProduct();
        try {
            localStorageManager.backupToFile(new File(getActivity().getFilesDir() + "/storage.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void configUsernameText(String username) {
        welcomeMessage = getView().findViewById(R.id.welcomeMessage);
        welcomeMessage.setText("Bentornato " + username);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUltimeAggiunte();
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
                                CollectionReference userProducts = db.collection("user").document(document.getId()).collection("product");
                                userProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().size() == 0) {
                                                Log.d("RESULT: ", "User's pantry of: " + userFromFile.getUsername() + " is empty");
                                            }
                                            ArrayList<Prodotto> userProductFromDb = new ArrayList<>();
                                            for (QueryDocumentSnapshot productDocument : task.getResult()) {
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                HashMap<String, Long> valoriNutrizionaliValues = (HashMap<String, java.lang.Long>) productDocument.getData().get("valoriNutrizionali");

                                                Long peso = (Long) productDocument.getData().get("peso");
                                                Long preparazione = (Long) productDocument.getData().get("preparazione");

                                                ArrayList dateScadenza = (ArrayList) productDocument.getData().get("dateScadenza");

                                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                ArrayList<ProductExpirationDate> productExpirationDates = new ArrayList<>();

                                                for (Object temp: dateScadenza) {
                                                    HashMap<String, String> currentData = (HashMap<String, String>) temp;
                                                    LocalDate date = LocalDate.parse(currentData.get("dataScadenza"), formatter);
                                                    productExpirationDates.add(new ProductExpirationDate(Integer.valueOf(currentData.get("amount")), date));
                                                }


                                                Prodotto tempProduct = null;
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                    tempProduct = new Prodotto(
                                                            (String) productDocument.getData().get("nome"),
                                                            (String) productDocument.getData().get("marca"),
                                                            (String) productDocument.getData().get("ingredienti"),
                                                            Integer.valueOf(peso.toString()),
                                                            (String) productDocument.getData().get("unità"),
                                                            Integer.valueOf(preparazione.toString()),
                                                            productExpirationDates,
                                                            (String) productDocument.getData().get("image"),
                                                            (String) productDocument.getData().get("colore"),
                                                            new ValoriNutrizionali(
                                                                    ((Number) valoriNutrizionaliValues.get("energia")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("energiaAR")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("grassi")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("grassiAR")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("grassiSaturi")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("grassiSaturiAR")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("carboidrati")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("carboidratiAR")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("zuccheri")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("zuccheriAR")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("fibre")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("fibreAR")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("proteine")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("proteineAR")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("sale")).doubleValue(),
                                                                    ((Number) valoriNutrizionaliValues.get("saleAR")).doubleValue()),
                                                            LocalDateTime.now()
                                                    );
                                                }

                                                userProductFromDb.add(tempProduct);
                                                Log.d("Product: ", productDocument.getId() + " ==> " + productDocument.getData());
                                                Log.d("Product cloned: ", tempProduct.toString());
                                            }
                                            }
                                            nonSoComeChiamarti(userProductFromDb);
                                        }
                                    }
                                });
                                Log.d("Result: ", document.getId() + " ==> " + document.getData());
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("cazzo");
                    }
                });
    }

    void getProdotti() {
        productCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
                                Log.d("RESULT: ", "User's pantry of: " + userFromFile.getUsername() + " is empty");
                            }
                            ArrayList<Prodotto> productFromDb = new ArrayList<>();
                            for (QueryDocumentSnapshot productDocument : task.getResult()) {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    HashMap<String, Long> valoriNutrizionaliValues = (HashMap<String, java.lang.Long>) productDocument.getData().get("valoriNutrizionali");

                                    Long peso = (Long) productDocument.getData().get("peso");
                                    Long preparazione = (Long) productDocument.getData().get("preparazione");

                                    ArrayList dateScadenza = (ArrayList) productDocument.getData().get("dateScadenza");

                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    ArrayList<ProductExpirationDate> productExpirationDates = new ArrayList<>();

                                    for (Object temp: dateScadenza) {
                                        HashMap<String, String> currentData = (HashMap<String, String>) temp;
                                        LocalDate date = LocalDate.parse(currentData.get("dataScadenza"), formatter);
                                        productExpirationDates.add(new ProductExpirationDate(Integer.valueOf(currentData.get("amount")), date));
                                    }


                                    Prodotto tempProduct = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        tempProduct = new Prodotto(
                                                (String) productDocument.getData().get("nome"),
                                                (String) productDocument.getData().get("marca"),
                                                (String) productDocument.getData().get("ingredienti"),
                                                Integer.valueOf(peso.toString()),
                                                (String) productDocument.getData().get("unità"),
                                                Integer.valueOf(preparazione.toString()),
                                                productExpirationDates,
                                                (String) productDocument.getData().get("image"),
                                                (String) productDocument.getData().get("colore"),
                                                new ValoriNutrizionali(
                                                        ((Number) valoriNutrizionaliValues.get("energia")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("energiaAR")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("grassi")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("grassiAR")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("grassiSaturi")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("grassiSaturiAR")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("carboidrati")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("carboidratiAR")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("zuccheri")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("zuccheriAR")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("fibre")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("fibreAR")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("proteine")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("proteineAR")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("sale")).doubleValue(),
                                                        ((Number) valoriNutrizionaliValues.get("saleAR")).doubleValue()),
                                                LocalDateTime.now()
                                        );
                                    }

                                    productFromDb.add(tempProduct);
                                    Log.d("Product: ", productDocument.getId() + " ==> " + productDocument.getData());
                                    Log.d("Product cloned: ", tempProduct.toString());
                                }
                            }
                            myProperties.setProdotti(productFromDb.toArray(new Prodotto[0]));
                        }
                    }
                });
    }


    void getUltimeAggiunte() {
        userCollection
                .whereEqualTo("username", userFromFile.getUsername())
                .whereEqualTo("password", userFromFile.getPassword())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() != 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    CollectionReference userProductsCollection = db.collection("user").document(document.getId()).collection("product");
                                    userProductsCollection
                                            .orderBy("dataAggiunta", Query.Direction.DESCENDING)
                                            .limit(4)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        if (task.getResult().size() == 0) {
                                                            Log.d("RESULT: ", "User's pantry of: " + userFromFile.getUsername() + " is empty");
                                                        }
                                                        ArrayList<Prodotto> userProductFromDb = new ArrayList<>();
                                                        for (QueryDocumentSnapshot productDocument : task.getResult()) {
                                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                                HashMap<String, Long> valoriNutrizionaliValues = (HashMap<String, java.lang.Long>) productDocument.getData().get("valoriNutrizionali");

                                                                Long peso = (Long) productDocument.getData().get("peso");
                                                                Long preparazione = (Long) productDocument.getData().get("preparazione");

                                                                ArrayList dateScadenza = (ArrayList) productDocument.getData().get("dateScadenza");

                                                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                                ArrayList<ProductExpirationDate> productExpirationDates = new ArrayList<>();

                                                                for (Object temp: dateScadenza) {
                                                                    HashMap<String, String> currentData = (HashMap<String, String>) temp;
                                                                    LocalDate date = LocalDate.parse(currentData.get("dataScadenza"), formatter);
                                                                    productExpirationDates.add(new ProductExpirationDate(Integer.valueOf(currentData.get("amount")), date));
                                                                }


                                                                Prodotto tempProduct = null;
                                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                                    tempProduct = new Prodotto(
                                                                            (String) productDocument.getData().get("nome"),
                                                                            (String) productDocument.getData().get("marca"),
                                                                            (String) productDocument.getData().get("ingredienti"),
                                                                            Integer.valueOf(peso.toString()),
                                                                            (String) productDocument.getData().get("unità"),
                                                                            Integer.valueOf(preparazione.toString()),
                                                                            productExpirationDates,
                                                                            (String) productDocument.getData().get("image"),
                                                                            (String) productDocument.getData().get("colore"),
                                                                            new ValoriNutrizionali(
                                                                                    ((Number) valoriNutrizionaliValues.get("energia")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("energiaAR")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("grassi")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("grassiAR")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("grassiSaturi")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("grassiSaturiAR")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("carboidrati")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("carboidratiAR")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("zuccheri")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("zuccheriAR")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("fibre")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("fibreAR")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("proteine")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("proteineAR")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("sale")).doubleValue(),
                                                                                    ((Number) valoriNutrizionaliValues.get("saleAR")).doubleValue()),
                                                                            LocalDateTime.now()
                                                                    );
                                                                }

                                                                userProductFromDb.add(tempProduct);
                                                                Log.d("Product: ", productDocument.getId() + " ==> " + productDocument.getData());
                                                                Log.d("Product cloned: ", tempProduct.toString());
                                                            }
                                                        }
                                                        setUltimeAggiunteAdapter(userProductFromDb);
                                                        nonSoComeChiamarti(userProductFromDb);
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }

    public void setUltimeAggiunteAdapter(ArrayList<Prodotto> ultimeAggiunteProducts) {
        ultimeAggiunteAdapter.setUltimeAggiunte(ultimeAggiunteProducts.toArray(new Prodotto[0]));
        ultimeAggiunteProdotti.setAdapter(ultimeAggiunteAdapter);
        ultimeAggiunteAdapter.notifyDataSetChanged();
    }

    void nonSoComeChiamarti(ArrayList<Prodotto> userProductsFromCollection) {
        userProducts = userProductsFromCollection;
    }
}
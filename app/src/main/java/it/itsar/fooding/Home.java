package it.itsar.fooding;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Home extends Fragment {

    private RecyclerView ultimeAggiunteProdotti;
    private RecyclerView ricetteConsigliate;
    private final MyProperties myProperties = MyProperties.getInstance();
    private ArrayList<Prodotto> ultimeAggiunte;
    private ArrayList<Ricetta> ricette;
    private LocalStorageManager localStorageManager = new LocalStorageManager();
    private UltimeAggiunteAdapter ultimeAggiunteAdapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("user");
    private CollectionReference productCollection = db.collection("product");

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private String userEmail;
    private String userUsername = "";

    private FirestoreManager firestoreManager;

    private User userFromFile;

    private TextView welcomeMessage;

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
        if (firebaseUser != null) {
            userEmail = firebaseUser.getEmail();
        }
        welcomeMessage = getView().findViewById(R.id.welcomeMessage);
        if (myProperties.getUserUsername() != null) {
            userUsername = myProperties.getUserUsername();
            welcomeMessage.setText("Bentornato " + userUsername);

        }
        ultimeAggiunte = new ArrayList<>();
        firestoreManager = new FirestoreManager();
        ultimeAggiunteProdotti = view.findViewById(R.id.ultimeAggiunteProdotti);
        ultimeAggiunteProdotti.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ultimeAggiunteAdapter = new UltimeAggiunteAdapter(ultimeAggiunte, getContext(), activityLauncher);
        if (myProperties.getUltimeAggiunte().size() == 0) {
            try {
                if (localStorageManager.backupFromFile(getActivity().getFilesDir() + localStorageManager.ULTIME_AGGIUNTE_FILE_NAME) != null) {
                    ultimeAggiunte = localStorageManager.backupFromFile(getActivity().getFilesDir() + localStorageManager.ULTIME_AGGIUNTE_FILE_NAME);
                    myProperties.setUltimeAggiunte(ultimeAggiunte);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ultimeAggiunte = myProperties.getUltimeAggiunte();
        }
        firestoreManager.getRecipes();


        if (ultimeAggiunte.size() != 0) {
            ultimeAggiunteAdapter = new UltimeAggiunteAdapter(ultimeAggiunte, getContext(), activityLauncher);
            ultimeAggiunteAdapter.setUltimeAggiunte(ultimeAggiunte);
            ultimeAggiunteProdotti.setAdapter(ultimeAggiunteAdapter);
        }

        if (firebaseUser != null) {
            firestoreManager.getUltimeAggiunte((productsFromCollection) -> {
                ultimeAggiunte = productsFromCollection;
                if (!Prodotto.makeComparable(myProperties.getUltimeAggiunte()).equals(Prodotto.makeComparable(ultimeAggiunte))) {
                    myProperties.setUltimeAggiunte(ultimeAggiunte);
                    ultimeAggiunteAdapter.setUltimeAggiunte(ultimeAggiunte);
                    ultimeAggiunteProdotti.setAdapter(ultimeAggiunteAdapter);
                    try {
                        localStorageManager.backupToFile(new File(getActivity().getFilesDir() + localStorageManager.ULTIME_AGGIUNTE_FILE_NAME), ultimeAggiunte);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        ricetteConsigliate = view.findViewById(R.id.ricetteConsigliate);
        ricetteConsigliate.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        HashMap<String, String> ingredienti1Ricetta1 = new HashMap<>();
        HashMap<String, String> ingredienti2Ricetta1 = new HashMap<>();
        HashMap<String, String> ingredienti3Ricetta1 = new HashMap<>();

        HashMap<String, String> ingredienti1Ricetta2 = new HashMap<>();

        HashMap<String, String> ingredienti1Ricetta3 = new HashMap<>();
        HashMap<String, String> ingredienti2Ricetta3 = new HashMap<>();
        HashMap<String, String> ingredienti3Ricetta3 = new HashMap<>();

        ingredienti1Ricetta1.put("nome", "Tagliatelle n. 203");
        ingredienti1Ricetta1.put("marca", "De Cecco");
        ingredienti1Ricetta1.put("peso", "100");
        ingredienti2Ricetta1.put("nome", "Ragù Contadino");
        ingredienti2Ricetta1.put("marca", "Barilla");
        ingredienti2Ricetta1.put("peso", "60");
        ingredienti3Ricetta1.put("nome", "Pepe nero");
        ingredienti3Ricetta1.put("marca", "Cannamela");
        ingredienti3Ricetta1.put("peso", "5");

        ingredienti1Ricetta2.put("nome", "Crema carciofi");
        ingredienti1Ricetta2.put("marca", "Knorr");
        ingredienti1Ricetta2.put("peso", "140");

        ingredienti1Ricetta3.put("nome", "Uova guscio bianco");
        ingredienti1Ricetta3.put("marca", "Le naturelle");
        ingredienti1Ricetta3.put("peso", "160");
        ingredienti2Ricetta3.put("nome", "Olio extra vergine di oliva");
        ingredienti2Ricetta3.put("marca", "Dante");
        ingredienti2Ricetta3.put("peso", "15");


/*
        ricette = new ArrayList<>(Arrays.asList(
                new Ricetta("Tagliatelle al ragù", R.drawable.tagliatelle_al_ragu, "GialloZafferano", firestoreManager.getProductsOfRecipes(new HashMap[]{ingredienti1Ricetta1, ingredienti2Ricetta1, ingredienti3Ricetta1}), 11, 324, Ricetta.Difficolta.FACILE),
                new Ricetta("Crema carciofi", R.drawable.crema_carciofi_ricetta, "Knorr", firestoreManager.getProductsOfRecipes(new HashMap[]{ingredienti1Ricetta2}), 8, 154, Ricetta.Difficolta.FACILE),
                new Ricetta("Omelette", R.drawable.omelette_ricetta, "BurroFuso", firestoreManager.getProductsOfRecipes(new HashMap[]{ingredienti1Ricetta3, ingredienti2Ricetta3}), 5, 287, Ricetta.Difficolta.FACILE)
        ));

 */

        RicetteConsigliateAdapter ricetteConsigliateAdapter = new RicetteConsigliateAdapter(ricette, getContext(), ricettaDetailsActivityLauncher);
        ricetteConsigliate.setAdapter(ricetteConsigliateAdapter);

    }

    void updateUserProducts(ActivityResult result) {
        Intent intent = result.getData();
        assert intent != null;
        int position = intent.getIntExtra("position", -1);
        Prodotto prodotto = (Prodotto) intent.getSerializableExtra("prodotto");
        if (!prodotto.getDateScadenza().toString().equals(ultimeAggiunte.get(position).getDateScadenza().toString())) {
            prodotto.checkEmptyExpirationDate();
            ultimeAggiunte.get(position).setDateScadenza(prodotto.getDateScadenza());
            firestoreManager.editProductInUserCollection(ultimeAggiunte.get(position), () -> {
                getUserProductsFromCollection();
            });
        }
    }

    void getUserProductsFromCollection() {
        firestoreManager.getUltimeAggiunte((userProductsFromCollection)->{
            ultimeAggiunte = userProductsFromCollection;
            if (!Prodotto.makeComparable(myProperties.getUserProdotti()).equals(Prodotto.makeComparable(ultimeAggiunte))) {
                myProperties.setUserProdotti(ultimeAggiunte);
                ultimeAggiunteAdapter.setUltimeAggiunte(ultimeAggiunte);
                ultimeAggiunteProdotti.setAdapter(ultimeAggiunteAdapter);
                try {
                    localStorageManager.backupToFile(new File(getActivity().getFilesDir() + "/storage.txt"), ultimeAggiunte);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void configUsernameText() {
        firestoreManager.getUsername((String usernameFromCollection) -> {
            if (!userUsername.equals(usernameFromCollection)) {
                userUsername = usernameFromCollection;
                myProperties.setUserUsername(userUsername);
                welcomeMessage.setText("Bentornato " + userUsername);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        configUsernameText();
    }
}
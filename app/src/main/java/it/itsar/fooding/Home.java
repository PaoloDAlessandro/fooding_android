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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirestoreManager firestoreManager;

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
                        //updateUserProducts(result);
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
        ultimeAggiunte = new ArrayList<>();
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


        if (ultimeAggiunte.size() != 0) {
            ultimeAggiunteAdapter = new UltimeAggiunteAdapter(ultimeAggiunte, getContext(), activityLauncher);
            ultimeAggiunteAdapter.setUltimeAggiunte(ultimeAggiunte);
            ultimeAggiunteProdotti.setAdapter(ultimeAggiunteAdapter);
        }

        if (firebaseUser != null) {
            firestoreManager = new FirestoreManager(getActivity().getFilesDir() + AuthStorageManager.AUTH_FILE_NAME);
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

        Log.d("RESULT:", "FATTO MANNAGGIA");
        ricetteConsigliate = view.findViewById(R.id.ricetteConsigliate);
        ricetteConsigliate.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        userFromFile = authStorageManager.backupFromFile(getActivity().getFilesDir() + AuthStorageManager.AUTH_FILE_NAME);
        configUsernameText(userFromFile.getUsername());

        /*
        ricette = new ArrayList<>(Arrays.asList(
                new Ricetta("Tagliatelle al ragù", R.drawable.tagliatelle_al_ragu, "GialloZafferano", new ArrayList<>(Arrays.asList(new Ingrediente(80, myProperties.getProdotti()[11]), new Ingrediente(60, myProperties.getProdotti()[2]), new Ingrediente(10, myProperties.getProdotti()[10]), new Ingrediente(5, myProperties.getProdotti()[5]))), 11, 324, Ricetta.Difficolta.FACILE),
                new Ricetta("Crema carciofi", R.drawable.crema_carciofi_ricetta, "Knorr", new ArrayList<>(Arrays.asList(new Ingrediente(88, myProperties.getProdotti()[0]))), 8, 154, Ricetta.Difficolta.FACILE),
                new Ricetta("Omelette", R.drawable.omelette_ricetta, "BurroFuso", new ArrayList<>(Arrays.asList(new Ingrediente(120, myProperties.getProdotti()[14]), new Ingrediente(15, myProperties.getProdotti()[10]), new Ingrediente(30, myProperties.getProdotti()[15]))), 5, 287, Ricetta.Difficolta.FACILE)
        ));
         */

        ricette = new ArrayList<>(Arrays.asList(
                new Ricetta("Tagliatelle al ragù", R.drawable.tagliatelle_al_ragu, "GialloZafferano", null, 11, 324, Ricetta.Difficolta.FACILE),
                new Ricetta("Crema carciofi", R.drawable.crema_carciofi_ricetta, "Knorr", null, 8, 154, Ricetta.Difficolta.FACILE),
                new Ricetta("Omelette", R.drawable.omelette_ricetta, "BurroFuso", null, 5, 287, Ricetta.Difficolta.FACILE)
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
            localStorageManager.backupToFile(new File(getActivity().getFilesDir() + "/storage.txt"), userProducts);
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
    }
}
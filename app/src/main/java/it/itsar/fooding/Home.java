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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Home extends Fragment {

    private RecyclerView ultimeAggiunteProdotti;
    private RecyclerView ricetteConsigliate;
    private final MyProperties myProperties = MyProperties.getInstance();
    private ArrayList<Prodotto> userProducts;
    private ArrayList<Ricetta> ricette = new ArrayList<>();
    private LocalStorageManager localStorageManager = new LocalStorageManager();
    private UltimeAggiunteAdapter ultimeAggiunteAdapter;

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
        userProducts = myProperties.getUserProdotti();
        ultimeAggiunteProdotti.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ricetteConsigliate = view.findViewById(R.id.ricetteConsigliate);
        ricetteConsigliate.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        orderByAdditionDate();

        ricette.add(new Ricetta("Tagliatelle al ragù", R.drawable.tagliatelle_al_ragu, "GialloZafferano", new ArrayList<>(Arrays.asList(new Ingrediente(80, myProperties.getProdotti()[11]), new Ingrediente(60, myProperties.getProdotti()[2]), new Ingrediente(10, myProperties.getProdotti()[10]), new Ingrediente(5, myProperties.getProdotti()[5]))), 11, 324, Ricetta.Difficolta.FACILE));

        RicetteConsigliateAdapter ricetteConsigliateAdapter = new RicetteConsigliateAdapter(ricette, getContext(), ricettaDetailsActivityLauncher);
        ricetteConsigliate.setAdapter(ricetteConsigliateAdapter);
        setUltimeAggiunteProdotti();

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
        setUltimeAggiunteProdotti();
    }

    void orderByAdditionDate() {
        userProducts.sort((p1, p2) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return p2.getDataAggiunta().compareTo(p1.getDataAggiunta());
            }
            return 0;
        });
    }


    void setUltimeAggiunteProdotti() {
        if(userProducts.size() != 0) {
            if(userProducts.size() > 4) {
                ultimeAggiunteAdapter = new UltimeAggiunteAdapter(userProducts.subList(0,4).toArray(new Prodotto[0]), getContext(), activityLauncher);
            }
            else {
                ultimeAggiunteAdapter = new UltimeAggiunteAdapter(userProducts.subList(0,userProducts.size()).toArray(new Prodotto[0]), getContext(), activityLauncher);
            }
            ultimeAggiunteProdotti.setAdapter(ultimeAggiunteAdapter);
        }
    }
}
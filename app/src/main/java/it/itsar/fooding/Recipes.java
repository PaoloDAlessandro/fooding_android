package it.itsar.fooding;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Recipes extends Fragment {

    private FirestoreManager firestoreManager = new FirestoreManager();
    private RecyclerView recipesRecyclerView;
    private ArrayList<Ricetta> ricette;
    private Spinner recipesSpinner;

    private RecipeAdapter recipeAdapter;
    private EditText recipesInput;

    private int filterMode;


    private ArrayList<Integer> spinnerItems = new ArrayList<>();

    private ActivityResultLauncher<Intent> ricettaDetailsActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipesInput = view.findViewById(R.id.searchRecipe);
        recipesRecyclerView = view.findViewById(R.id.recipesRecycleView);

        recipesSpinner = view.findViewById(R.id.recipesSpinner);

        spinnerItems.add(R.drawable.chef_icon);
        spinnerItems.add(R.drawable.bolt_icon);
        spinnerItems.add(R.drawable.product_timer);

        FilterItemAdapter adapter = new FilterItemAdapter(getContext(), R.layout.filter_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(R.layout.filter_spinner_item);
        recipesSpinner.setAdapter(adapter);

        recipesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterMode = adapter.getItem(i);
                filterProductsManager();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        HashMap<String, String> ingredienti1Ricetta4 = new HashMap<>();
        HashMap<String, String> ingredienti2Ricetta4 = new HashMap<>();

        ingredienti1Ricetta4.put("nome", "Riso");
        ingredienti1Ricetta4.put("marca", "Gallo");
        ingredienti1Ricetta4.put("peso", "120");
        ingredienti2Ricetta4.put("nome", "Pepe nero");
        ingredienti2Ricetta4.put("marca", "Cannamela");
        ingredienti2Ricetta4.put("peso", "5");




        ricette = new ArrayList<>(Arrays.asList(
                new Ricetta("Tagliatelle al ragù", R.drawable.tagliatelle_al_ragu, "GialloZafferano", firestoreManager.getProductsOfRecipes(new HashMap[]{ingredienti1Ricetta1, ingredienti2Ricetta1, ingredienti3Ricetta1}), 11, 324, Ricetta.Difficolta.FACILE),
                new Ricetta("Crema carciofi", R.drawable.crema_carciofi_ricetta, "Knorr", firestoreManager.getProductsOfRecipes(new HashMap[]{ingredienti1Ricetta2}), 8, 154, Ricetta.Difficolta.FACILE),
                new Ricetta("Risotto", R.drawable.risotto, "Cookidoo", firestoreManager.getProductsOfRecipes(new HashMap[]{ingredienti1Ricetta4, ingredienti2Ricetta4}), 30, 433, Ricetta.Difficolta.MEDIA),
                new Ricetta("Omelette", R.drawable.omelette_ricetta, "BurroFuso", firestoreManager.getProductsOfRecipes(new HashMap[]{ingredienti1Ricetta3, ingredienti2Ricetta3}), 5, 287, Ricetta.Difficolta.FACILE)
        ));


        recipeAdapter = new RecipeAdapter(ricette, getContext(), ricettaDetailsActivityLauncher);
        recipesRecyclerView.setAdapter(recipeAdapter);
        filterByDifficolta();
        filterInputRecipeManager(recipesInput.getText().toString());

        recipesInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterInputRecipeManager(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    void filterProductsManager() {
        switch (filterMode) {
            case (R.drawable.chef_icon):
                filterByDifficolta();
                break;

            case (R.drawable.bolt_icon):
                filterByKcal();
                break;

            case (R.drawable.product_timer):
                filterByTempoPreparazione();
                break;
        }

        filterInputRecipeManager(recipesInput.getText().toString());

    }

    void filterByDifficolta() {
        ArrayList<Ricetta> ricetteDaOrdinare = ricette;

        ricetteDaOrdinare.sort((r1, r2) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return r1.getDifficolta().compareTo(r2.getDifficolta());
            }
            return 0;
        });

        recipeAdapter.setRicette(ricetteDaOrdinare);
        recipeAdapter.notifyDataSetChanged();
    }

    void filterByTempoPreparazione() {
        ArrayList<Ricetta> ricetteDaOrdinare = ricette;

        ricetteDaOrdinare.sort((r1, r2) -> r2.getKcal() - r1.getKcal());

        recipeAdapter.setRicette(ricetteDaOrdinare);
        recipeAdapter.notifyDataSetChanged();
    }

    void filterByKcal() {
        ArrayList<Ricetta> ricetteDaOrdinare = ricette;

        ricetteDaOrdinare.sort(Comparator.comparingInt(Ricetta::getKcal));

        recipeAdapter.setRicette(ricetteDaOrdinare);
        recipeAdapter.notifyDataSetChanged();
    }

    void filterInputRecipeManager(CharSequence charSequence) {
        ArrayList<Ricetta> ricetteFiltered = ricette;
        ricetteFiltered = (ArrayList<Ricetta>) ricetteFiltered.stream()
                .filter(ricetta -> ricetta.getNome().toLowerCase().contains(charSequence.toString().toLowerCase()))
                .collect(Collectors.toList());
        recipeAdapter.setRicette(ricetteFiltered);
        recipesRecyclerView.setAdapter(recipeAdapter);
    }

}
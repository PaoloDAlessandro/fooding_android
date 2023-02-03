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

import java.io.File;
import java.io.IOException;
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

    private MyProperties myProperties = MyProperties.getInstance();
    private LocalStorageManager localStorageManager = new LocalStorageManager();

    private RecipeAdapter recipeAdapter;
    private EditText recipesInput;

    private int filterMode;

    private FilterItemAdapter adapter;


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

        adapter = new FilterItemAdapter(getContext(), R.layout.filter_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(R.layout.filter_spinner_item);
        recipesSpinner.setAdapter(adapter);

        if (myProperties.getRicette().size() == 0) {
            try {
                if (localStorageManager.backupFromRecipesFile(getActivity().getFilesDir() + localStorageManager.RICETTE_FILE_NAME) != null) {
                    ricette = localStorageManager.backupFromRecipesFile(getActivity().getFilesDir() + localStorageManager.RICETTE_FILE_NAME);
                    myProperties.setRicette(ricette);
                    initRecipesAdapter();
                    compareLocalWithRecipeCollection();
                }
                else {
                    firestoreManager.getRecipes((recipesFromCollection) -> {
                        ricette = recipesFromCollection;
                        initRecipesAdapter();
                        myProperties.setRicette(ricette);
                        recipeAdapter.setRicette(ricette);
                        recipesRecyclerView.setAdapter(recipeAdapter);
                        try {
                            localStorageManager.backupToRecipesFile(new File(getActivity().getFilesDir() + localStorageManager.RICETTE_FILE_NAME), ricette);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ricette = myProperties.getRicette();
            initRecipesAdapter();
            compareLocalWithRecipeCollection();
        }


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

    void compareLocalWithRecipeCollection() {
        firestoreManager.getRecipes((recipesFromCollection) -> {
            if (ricette.retainAll(recipesFromCollection)) {
                ricette = recipesFromCollection;
                myProperties.setRicette(ricette);
                initRecipesAdapter();
            }
            try {
                localStorageManager.backupToRecipesFile(new File(getActivity().getFilesDir() + localStorageManager.RICETTE_FILE_NAME), ricette);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    void initRecipesAdapter() {
        recipeAdapter = new RecipeAdapter(ricette, getContext(), ricettaDetailsActivityLauncher);
        recipesRecyclerView.setAdapter(recipeAdapter);
        filterByDifficolta();
        filterInputRecipeManager(recipesInput.getText().toString());
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

        ricetteDaOrdinare.sort(Comparator.comparingInt(Ricetta::getTempoPreparazione));

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
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
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Pantry extends Fragment {

    private RecyclerView recyclerView;

    public MyProperties myProperties = MyProperties.getInstance();
    private Prodotto[] prodotti = myProperties.getUserProdotti().toArray(new Prodotto[0]);
    private Spinner productFilter;
    private Button addProductButton;

    private int filterMode;
    private ProductAdapter productAdapter;
    private EditText searchProduct;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                switch (result.getResultCode()){
                    case Activity.RESULT_OK:
                        updateRecycleView(result);
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
        return inflater.inflate(R.layout.fragment_pantry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productAdapter = new ProductAdapter(prodotti, getContext(), activityLauncher);
        searchProduct = view.findViewById(R.id.searchProduct);
        recyclerView = view.findViewById(R.id.productsRecyclerView);
        productFilter = view.findViewById(R.id.productFilter);
        addProductButton = view.findViewById(R.id.addProductButton);
        recyclerView.setAdapter(productAdapter);

        filterByExpiration();
        setAddProductButtonTouchListener();

        List<Integer> productFilterItems = new ArrayList<>();
        productFilterItems.add(R.drawable.calendar_expiration_date);
        productFilterItems.add(R.drawable.box_closed);
        productFilterItems.add(R.drawable.alphabeth_a_z);
        FilterItemAdapter adapter = new FilterItemAdapter(getContext(), R.layout.filter_spinner_item, productFilterItems);

        adapter.setDropDownViewResource(R.layout.filter_spinner_item);
        productFilter.setAdapter(adapter);
        productFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterMode = adapter.getItem(i);
                filterProductsManager();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterInputProductManager(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    void filterInputProductManager(CharSequence charSequence) {
        List<Prodotto> prodottiFiltered = Arrays.asList(prodotti);
        prodottiFiltered = prodottiFiltered.stream()
                .filter(prodotto -> prodotto.getNome().toLowerCase().contains(charSequence.toString().toLowerCase()))
                .collect(Collectors.toList());
        Log.d("Input: ", charSequence.toString());
        Log.d("Filtered product: ", prodottiFiltered.toString());
        productAdapter.setProdotti(prodottiFiltered.toArray(new Prodotto[0]));
        recyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!searchProduct.getText().toString().equals("")) {
            filterInputProductManager(searchProduct.getText());
        }
    }


    void filterByExpiration() {
        List<Prodotto> prodottiDaOrdinare = Arrays.asList(prodotti);

        prodottiDaOrdinare.sort((p1, p2) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return p1.getDataScadenza().compareTo(p2.getDataScadenza());
            }
            return 0;
        });

        productAdapter.setProdotti(prodottiDaOrdinare.toArray(new Prodotto[0]));
        recyclerView.setAdapter(productAdapter);
    }

    void filterByAlphabet() {
        List<Prodotto> prodottiDaOrdinare = Arrays.asList(prodotti);

        prodottiDaOrdinare.sort(Comparator.comparing(Prodotto::getNome));

        productAdapter.setProdotti(prodottiDaOrdinare.toArray(new Prodotto[0]));
        recyclerView.setAdapter(productAdapter);

    }

    void filterByStock() {
        List<Prodotto> prodottiDaOrdinare = Arrays.asList(prodotti);

        prodottiDaOrdinare.sort((p1, p2) -> p2.getGiacenza() - p1.getGiacenza());

        productAdapter.setProdotti(prodottiDaOrdinare.toArray(new Prodotto[0]));
        recyclerView.setAdapter(productAdapter);

    }

    void updateRecycleView(ActivityResult result) {
        Intent intent = result.getData();
        assert intent != null;
        Prodotto prodotto = (Prodotto) intent.getSerializableExtra("prodotto");
        int position = intent.getIntExtra("position", 0);
        prodotti[position].setGiacenza(prodotto.getGiacenza());
        productAdapter.setProdotti(prodotti);
        recyclerView.setAdapter(productAdapter);
        filterProductsManager();
    }

    void filterProductsManager() {
        switch (filterMode) {
            case (R.drawable.calendar_expiration_date):
                filterByExpiration();
                break;

            case (R.drawable.box_closed):
                filterByStock();
                break;

            case (R.drawable.alphabeth_a_z):
                filterByAlphabet();
                break;
        }
    }

    void setAddProductButtonTouchListener() {
        addProductButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ProductAddition.class);
            activityLauncher.launch(intent);
        });
    }

}
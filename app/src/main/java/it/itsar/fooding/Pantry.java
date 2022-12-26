package it.itsar.fooding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Pantry extends Fragment {

    private RecyclerView recyclerView;

    public MyProperties myProperties = MyProperties.getInstance();
    private Prodotto[] prodotti = myProperties.getProdotti();

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

    private ProductAdapter productAdapter;
    private EditText searchProduct;

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
        recyclerView.setAdapter(productAdapter);

        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Prodotto> prodottiFiltered = Arrays.asList(prodotti);
                prodottiFiltered = prodottiFiltered.stream()
                        .filter(prodotto -> prodotto.getNome().toLowerCase().contains(charSequence.toString().toLowerCase()))
                        .collect(Collectors.toList());
                Log.d("Input: ", charSequence.toString());
                Log.d("Filtered product: ", prodottiFiltered.toString());
                productAdapter.setProdotti(prodottiFiltered.toArray(new Prodotto[prodottiFiltered.size()]));
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!searchProduct.getText().toString().equals("")) {
            List<Prodotto> prodottiFiltered = Arrays.asList(prodotti);
            prodottiFiltered = prodottiFiltered.stream()
                    .filter(prodotto -> prodotto.getNome().toLowerCase().contains(searchProduct.getText().toString().toLowerCase()))
                    .collect(Collectors.toList());
            Log.d("Input: ", searchProduct.getText().toString());
            Log.d("Filtered product: ", prodottiFiltered.toString());
            productAdapter.setProdotti(prodottiFiltered.toArray(new Prodotto[prodottiFiltered.size()]));
            recyclerView.setAdapter(productAdapter);
        }
    }

    void updateRecycleView(ActivityResult result) {
        Intent intent = result.getData();
        Prodotto prodotto = (Prodotto) intent.getSerializableExtra("prodotto");
        int position = intent.getIntExtra("position", 0);
        prodotti[position].setGiacenza(prodotto.getGiacenza());
        productAdapter.setProdotti(prodotti);
        recyclerView.setAdapter(productAdapter);

    }

}
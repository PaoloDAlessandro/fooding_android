package it.itsar.fooding;

import android.content.Intent;
import android.os.Bundle;

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
    public Prodotto[] prodotti = {
            new Prodotto("Crema carciofi", "Knorr", 250, 4, 3, R.drawable.crema_carciofi),
            new Prodotto("Ragù Contadino", "Barilla", 400, 1, 0, R.drawable.ragu_contadino),
            new Prodotto("Leerdammer", "Leerdammer", 350, 2, 0, R.drawable.leerdammer),
            new Prodotto("Rosette","Schär", 500, 5, 8, R.drawable.rosette),new Prodotto("Crema carciofi", "Knorr", 250, 4, 3, R.drawable.crema_carciofi),
            new Prodotto("Ragù Contadino", "Barilla", 400, 1, 0, R.drawable.ragu_contadino),
            new Prodotto("Leerdammer", "Leerdammer", 350, 2, 0, R.drawable.leerdammer),
            new Prodotto("Rosette","Schär", 500, 5, 8, R.drawable.rosette),new Prodotto("Crema carciofi", "Knorr", 250, 4, 3, R.drawable.crema_carciofi),
            new Prodotto("Ragù Contadino", "Barilla", 400, 1, 0, R.drawable.ragu_contadino),
            new Prodotto("Leerdammer", "Leerdammer", 350, 2, 0, R.drawable.leerdammer),
            new Prodotto("Rosette","Schär", 500, 5, 8, R.drawable.rosette),
            new Prodotto("Rosette","Schär", 500, 5, 8, R.drawable.rosette),
            new Prodotto("Rosette","Schär", 500, 5, 8, R.drawable.rosette)
    };

    private EditText searchProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pantry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My pantry");
        searchProduct = view.findViewById(R.id.searchProduct);
        recyclerView = view.findViewById(R.id.productsRecyclerView);
        ProductAdapter productAdapter = new ProductAdapter(prodotti);
        recyclerView.setAdapter(productAdapter);

        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Prodotto> prodottiFiltered = Arrays.asList(prodotti);
                prodottiFiltered = prodottiFiltered.stream().filter(prodotto -> prodotto.getNome().toLowerCase().contains(charSequence.toString().toLowerCase())).collect(Collectors.toList());
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

}
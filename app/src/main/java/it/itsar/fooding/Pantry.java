package it.itsar.fooding;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Pantry extends Fragment {

    private Prodotto[] prodotti = {
            new Prodotto("Crema carciofi", "Knorr", 250, 4, 20, R.drawable.crema_carciofi),
            new Prodotto("Crema carciofi", "Knorr", 250, 4, 20, R.drawable.crema_carciofi),
            new Prodotto("Crema carciofi", "Knorr", 250, 4, 20, R.drawable.crema_carciofi),
            new Prodotto("Crema carciofi", "Knorr", 250, 4, 20, R.drawable.crema_carciofi)
    };

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pantry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.productsRecyclerView);
        ProductAdapter productAdapter = new ProductAdapter(prodotti);
        recyclerView.setAdapter(productAdapter);
    }

}
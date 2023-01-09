package it.itsar.fooding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;

public class Home extends Fragment {

    private RecyclerView ultimeAggiunteProdotti;
    private final MyProperties myProperties = MyProperties.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ultimeAggiunteProdotti = view.findViewById(R.id.ultimeAggiunteProdotti);

        ultimeAggiunteProdotti.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        UltimeAggiunteAdapter ultimeAggiunteAdapter = new UltimeAggiunteAdapter(myProperties.getProdotti());
        ultimeAggiunteProdotti.setAdapter(ultimeAggiunteAdapter);

    }
}
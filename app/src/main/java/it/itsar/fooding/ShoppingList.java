package it.itsar.fooding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShoppingList extends Fragment {

    private RecyclerView shoppingList;
    private FloatingActionButton addShoppingListItem;

    private MyProperties myProperties = MyProperties.getInstance();

    private FirestoreManager firestoreManager = new FirestoreManager();

    ActivityResultLauncher<Intent> addProductToShoppingListActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                switch (result.getResultCode()){
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
            }
    );


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shoppingList = view.findViewById(R.id.shoppingList);
        firestoreManager.getUserShoppingList((shoppingListItemsFromCollection) -> {
            ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter(shoppingListItemsFromCollection);
            shoppingList.setAdapter(shoppingListAdapter);
        });

        addShoppingListItem = view.findViewById(R.id.addShoppingListItem);


        addShoppingListItem.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), ShoppingListProductAddition.class);
            addProductToShoppingListActivityLauncher.launch(intent);
        });


    }
}
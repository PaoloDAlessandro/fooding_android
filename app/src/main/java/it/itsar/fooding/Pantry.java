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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Pantry extends Fragment {

    private RecyclerView recyclerView;

    public MyProperties myProperties = MyProperties.getInstance();
    private Spinner productFilter;
    private FloatingActionButton addProductButton;
    private ArrayList<Prodotto> userProducts = new ArrayList<>();

    private LocalStorageManager localStorageManager = new LocalStorageManager();

    private FirestoreManager firestoreManager;

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
                        break;
                }
            }
    );

    ActivityResultLauncher<Intent> addProductActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                switch (result.getResultCode()){
                    case Activity.RESULT_OK:
                        getUserProductsFromCollection();
                        showAdditionSuccessSnackBar();
                        break;
                    case Activity.RESULT_CANCELED:
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
        searchProduct = view.findViewById(R.id.searchProduct);
        recyclerView = view.findViewById(R.id.productsRecyclerView);
        productFilter = view.findViewById(R.id.productFilter);
        addProductButton = view.findViewById(R.id.addProductButton);

        if (myProperties.getUserProdotti().size() == 0) {
            try {
                if (localStorageManager.backupFromFile(getActivity().getFilesDir() + localStorageManager.USER_PRODUCT_FILE_NAME) != null) {
                    userProducts = localStorageManager.backupFromFile(getActivity().getFilesDir() + localStorageManager.USER_PRODUCT_FILE_NAME);
                } else {
                    userProducts = new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            userProducts = myProperties.getUserProdotti();
        }
        productAdapter = new ProductAdapter(userProducts, getContext(), activityLauncher);

        if (userProducts.size() != 0) {
            filterByExpiration();
            productAdapter.setProdotti(userProducts);
            recyclerView.setAdapter(productAdapter);
        }

        firestoreManager = new FirestoreManager();
        getUserProductsFromCollection();
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
        ArrayList<Prodotto> prodottiFiltered = userProducts;
        prodottiFiltered = (ArrayList<Prodotto>) prodottiFiltered.stream()
                .filter(prodotto -> prodotto.getNome().toLowerCase().startsWith(charSequence.toString().toLowerCase()))
                .collect(Collectors.toList());
        productAdapter.setProdotti(prodottiFiltered);
        recyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!searchProduct.getText().toString().equals("")) {
            filterInputProductManager(searchProduct.getText());
        }
        getUserProductsFromCollection();
    }

    void filterByExpiration() {
        ArrayList<Prodotto> prodottiDaOrdinare = userProducts;

        prodottiDaOrdinare.sort((p1, p2) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return p1.getCloserExpirationdate().getExpirationDate().compareTo(p2.getCloserExpirationdate().getExpirationDate());
            }
            return 0;
        });

        productAdapter.setProdotti(prodottiDaOrdinare);
        productAdapter.notifyDataSetChanged();
    }

    void filterByAlphabet() {
        ArrayList<Prodotto> prodottiDaOrdinare = userProducts;

        prodottiDaOrdinare.sort(Comparator.comparing(Prodotto::getNome));

        productAdapter.setProdotti(prodottiDaOrdinare);
        productAdapter.notifyDataSetChanged();
    }

    void filterByStock() {
        ArrayList<Prodotto> prodottiDaOrdinare = userProducts;

        prodottiDaOrdinare.sort((p1, p2) -> p2.getAmountOfUnits() - p1.getAmountOfUnits());

        productAdapter.setProdotti(prodottiDaOrdinare);
        productAdapter.notifyDataSetChanged();
    }

    void updateRecycleView(ActivityResult result) {
        Intent intent = result.getData();
        assert intent != null;
        int position = intent.getIntExtra("position", -1);
        Prodotto prodotto = (Prodotto) intent.getSerializableExtra("prodotto");
        if (!prodotto.getDateScadenza().toString().equals(userProducts.get(position).getDateScadenza().toString())) {
            prodotto.checkEmptyExpirationDate();
            userProducts.get(position).setDateScadenza(prodotto.getDateScadenza());
            firestoreManager.editProductInUserCollection(userProducts.get(position), () -> {
                getUserProductsFromCollection();
            });
            }
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

        filterInputProductManager(searchProduct.getText().toString());

    }

    void getUserProductsFromCollection() {
        firestoreManager.getUserProducts((userProductsFromCollection)->{
            userProducts = userProductsFromCollection;
            if (!Prodotto.makeComparable(myProperties.getUserProdotti()).equals(Prodotto.makeComparable(userProducts))) {
                myProperties.setUserProdotti(userProducts);
                filterProductsManager();
                filterInputProductManager(searchProduct.getText().toString());
                try {
                    localStorageManager.backupToFile(new File(getActivity().getFilesDir() + "/storage.txt"), userProducts);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void showAdditionSuccessSnackBar() {
        Snackbar.make(this.requireView(), "Prodotto aggiunto correttamente!", Snackbar.LENGTH_SHORT).show();
    }

    void setAddProductButtonTouchListener() {
        addProductButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ProductAddition.class);
            addProductActivityLauncher.launch(intent);
        });
    }
}
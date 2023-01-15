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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Pantry extends Fragment {

    private RecyclerView recyclerView;

    public MyProperties myProperties = MyProperties.getInstance();
    private Spinner productFilter;
    private Button addProductButton;
    private ArrayList<Prodotto> userProducts = new ArrayList<>();

    private LocalStorageManager localStorageManager = new LocalStorageManager();
    private AuthStorageManager authStorageManager = new AuthStorageManager();

    private int filterMode;
    private ProductAdapter productAdapter;
    private EditText searchProduct;

    private User userFromFile;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference userCollection = db.collection("user");
    private final CollectionReference productCollection = db.collection("product");

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

    ActivityResultLauncher<Intent> addProductActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                switch (result.getResultCode()){
                    case Activity.RESULT_OK:
                        updateFromCollection("dateScadenza");
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
        userFromFile = authStorageManager.backupFromFile(getActivity().getFilesDir() + AuthStorageManager.AUTH_FILE_NAME);
        updateFromCollection("dateScadenza");
        productAdapter = new ProductAdapter(userProducts, getContext(), activityLauncher);
        searchProduct = view.findViewById(R.id.searchProduct);
        recyclerView = view.findViewById(R.id.productsRecyclerView);
        productFilter = view.findViewById(R.id.productFilter);
        addProductButton = view.findViewById(R.id.addProductButton);
        recyclerView.setAdapter(productAdapter);
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
                .filter(prodotto -> prodotto.getNome().toLowerCase().contains(charSequence.toString().toLowerCase()))
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
        updateFromCollection("dateScadenza");
        filterProductsManager();
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
        recyclerView.setAdapter(productAdapter);

    }

    void filterByStock() {
        ArrayList<Prodotto> prodottiDaOrdinare = userProducts;

        prodottiDaOrdinare.sort((p1, p2) -> p2.getAmountOfUnits() - p1.getAmountOfUnits());

        productAdapter.setProdotti(prodottiDaOrdinare);
        recyclerView.setAdapter(productAdapter);

    }

    void updateRecycleView(ActivityResult result) {
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
        productAdapter.setProdotti(userProducts);
        recyclerView.setAdapter(productAdapter);
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

    void showAdditionSuccessSnackBar() {
        Snackbar.make(this.requireView(), "Prodotto aggiunto correttamente!", Snackbar.LENGTH_SHORT).show();
    }

    void setAddProductButtonTouchListener() {
        addProductButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ProductAddition.class);
            addProductActivityLauncher.launch(intent);
        });
    }

    void updateFromCollection(String orderByField) {
            userCollection.whereEqualTo("username", userFromFile.getUsername())
                    .whereEqualTo("password", userFromFile.getPassword())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    CollectionReference userProducts = db.collection("user").document(document.getId()).collection("product");
                                    userProducts.orderBy(orderByField, Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().size() == 0) {
                                                    Log.d("RESULT: ", "User's pantry of: " + userFromFile.getUsername() + " is empty");
                                                }
                                                ArrayList<Prodotto> userProductFromDb = new ArrayList<>();
                                                for (QueryDocumentSnapshot productDocument : task.getResult()) {
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                        HashMap<String, Long> valoriNutrizionaliValues = (HashMap<String, java.lang.Long>) productDocument.getData().get("valoriNutrizionali");

                                                        Long peso = (Long) productDocument.getData().get("peso");
                                                        Long preparazione = (Long) productDocument.getData().get("preparazione");

                                                        ArrayList dateScadenza = (ArrayList) productDocument.getData().get("dateScadenza");

                                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                        ArrayList<ProductExpirationDate> productExpirationDates = new ArrayList<>();

                                                        for (Object temp: dateScadenza) {
                                                            HashMap<String, String> currentData = (HashMap<String, String>) temp;
                                                            LocalDate date = LocalDate.parse(currentData.get("dataScadenza"), formatter);
                                                            productExpirationDates.add(new ProductExpirationDate(Integer.valueOf(currentData.get("amount")), date));
                                                        }


                                                        Prodotto tempProduct = null;
                                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                            tempProduct = new Prodotto(
                                                                    (String) productDocument.getData().get("nome"),
                                                                    (String) productDocument.getData().get("marca"),
                                                                    (String) productDocument.getData().get("ingredienti"),
                                                                    Integer.valueOf(peso.toString()),
                                                                    (String) productDocument.getData().get("unità"),
                                                                    Integer.valueOf(preparazione.toString()),
                                                                    productExpirationDates,
                                                                    (String) productDocument.getData().get("image"),
                                                                    (String) productDocument.getData().get("colore"),
                                                                    new ValoriNutrizionali(
                                                                            ((Number) valoriNutrizionaliValues.get("energia")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("energiaAR")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("grassi")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("grassiAR")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("grassiSaturi")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("grassiSaturiAR")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("carboidrati")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("carboidratiAR")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("zuccheri")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("zuccheriAR")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("fibre")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("fibreAR")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("proteine")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("proteineAR")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("sale")).doubleValue(),
                                                                            ((Number) valoriNutrizionaliValues.get("saleAR")).doubleValue()),
                                                                    LocalDateTime.now()
                                                            );
                                                        }

                                                        userProductFromDb.add(tempProduct);
                                                        Log.d("Product: ", productDocument.getId() + " ==> " + productDocument.getData());
                                                        Log.d("Product cloned: ", tempProduct.toString());
                                                    }
                                                }
                                                setUserProductToUserCollection(userProductFromDb);
                                                filterByExpiration();
                                            }
                                        }
                                    });
                                    Log.d("Result: ", document.getId() + " ==> " + document.getData());
                                }
                            }
                        }
                    });
        }

        void setUserProductToUserCollection(ArrayList<Prodotto> userProductsFromCollection) {
            userProducts = userProductsFromCollection;
            productAdapter.setProdotti(userProducts);
            recyclerView.setAdapter(productAdapter);
        }
}
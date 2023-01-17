package it.itsar.fooding;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirestoreManager {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference userCollection = db.collection("user");
    private final CollectionReference productCollection = db.collection("product");
    private AuthStorageManager authStorageManager = new AuthStorageManager();
    private User userFromFile;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    public FirestoreManager(String authFilePath) {
        userFromFile = authStorageManager.backupFromFile(authFilePath);
    }

    public void getUltimeAggiunte(FirestoreManagerCallback firestoreManagerCallback) {
        if (firebaseUser != null) {
            String userEmail = firebaseUser.getEmail();
            userCollection
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        CollectionReference userProductsCollection = db.collection("user").document(document.getId()).collection("product");
                                        userProductsCollection
                                                .orderBy("dataAggiunta", Query.Direction.DESCENDING)
                                                .limit(4)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().size() == 0) {
                                                                Log.d("RESULT: ", "User's pantry of: " + userFromFile.getUsername() + " is empty");
                                                            }
                                                            ArrayList<Prodotto> productsFromCollection = new ArrayList<>();
                                                            for (QueryDocumentSnapshot productDocument : task.getResult()) {
                                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                                    Prodotto tempProduct = trasformCollectionProduct(productDocument);
                                                                    productsFromCollection.add(tempProduct);
                                                                }
                                                            }
                                                            firestoreManagerCallback.onFinish(productsFromCollection);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
        }
    }


    public void getUserProducts(FirestoreManagerCallback firestoreManagerCallback) {
        if (firebaseUser != null) {
            String userEmail = firebaseUser.getEmail();
            userCollection
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    CollectionReference userProducts = db.collection("user").document(document.getId()).collection("product");
                                    userProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().size() == 0) {
                                                    Log.d("RESULT: ", "User's pantry of: " + userFromFile.getUsername() + " is empty");
                                                }
                                                ArrayList<Prodotto> userProductsFromCollection = new ArrayList<>();
                                                for (QueryDocumentSnapshot productDocument : task.getResult()) {
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                        Prodotto tempProduct = trasformCollectionProduct(productDocument);
                                                        userProductsFromCollection.add(tempProduct);
                                                    }
                                                }
                                                firestoreManagerCallback.onFinish(userProductsFromCollection);
                                            }
                                        }
                                    });
                                    Log.d("Result: ", document.getId() + " ==> " + document.getData());
                                }
                            }
                        }
                    });
        }
    }


    public void getProdotti(FirestoreManagerCallback firestoreManagerCallback) {
        productCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
                                Log.d("RESULT: ", "User's pantry of: " + userFromFile.getUsername() + " is empty");
                            }
                            ArrayList<Prodotto> productFromCollection = new ArrayList<>();
                            for (QueryDocumentSnapshot productDocument : task.getResult()) {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    Prodotto tempProduct = trasformCollectionProduct(productDocument);
                                    productFromCollection.add(tempProduct);
                                }
                            }
                            firestoreManagerCallback.onFinish(productFromCollection);
                        }
                    }
                });
    }
/*
    public void addProductToUserCollection(Prodotto userProduct, OnProductAddition onProductAddition) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ArrayList<HashMap<String, String>> dateScadenza = new ArrayList<>();
            HashMap<String, String> dataScadenza = new HashMap<>();
            dataScadenza.put("amount", String.valueOf(userProduct.getDateScadenza().get(0).getAmount()));
            dataScadenza.put("dataScadenza",userProduct.getDateScadenza().get(0).getExpirationDate().toString());
            dateScadenza.add(dataScadenza);
            Map<String, Object> data = new HashMap<>();
            data.put("nome", userProduct.getNome());
            data.put("marca", userProduct.getMarca());
            data.put("ingredienti", userProduct.getIngredienti());
            data.put("preparazione", userProduct.getPreparazione());
            data.put("unità", userProduct.getUnità());
            data.put("colore", userProduct.getColore());
            data.put("image", userProduct.getImage());
            data.put("peso", userProduct.getPeso());
            data.put("dataAggiunta", Timestamp.now());
            data.put("dateScadenza", dateScadenza);
            data.put("valoriNutrizionali", userProduct.getValoriNutrizionali());

            userCollection
                    .whereEqualTo("username", userFromFile.getUsername())
                    .whereEqualTo("password", userFromFile.getPassword())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    CollectionReference userProducts = db.collection("user").document(document.getId()).collection("product");
                                    userProducts.add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    try {
                                                        onProductAddition.onSuccess();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Log.d("Result: ", "ma pe davvero?");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("Result: ", "te pareva");
                                                }
                                            });
                                }
                            }
                        }
                    });
        }
    }

 */
/*
    void editProductInUserCollection(Prodotto userProduct) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ArrayList<HashMap<String, String>> dateScadenza = new ArrayList<>();
            for (ProductExpirationDate productExpirationDate:userProduct.getDateScadenza()) {
                HashMap<String, String> dataScadenza = new HashMap<>();
                dataScadenza.put("amount", String.valueOf(productExpirationDate.getAmount()));
                dataScadenza.put("dataScadenza", String.valueOf(productExpirationDate.getExpirationDate()));
                dateScadenza.add(dataScadenza);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("nome", userProduct.getNome());
            data.put("marca", userProduct.getMarca());
            data.put("ingredienti", userProduct.getIngredienti());
            data.put("preparazione", userProduct.getPreparazione());
            data.put("unità", userProduct.getUnità());
            data.put("colore", userProduct.getColore());
            data.put("image", userProduct.getImage());
            data.put("peso", userProduct.getPeso());
            data.put("dataAggiunta", Timestamp.now());
            data.put("dateScadenza", dateScadenza);
            data.put("valoriNutrizionali", userProduct.getValoriNutrizionali());

            userCollection
                    .whereEqualTo("username", userFromFile.getUsername())
                    .whereEqualTo("password", userFromFile.getPassword())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    CollectionReference userProducts = db.collection("user").document(document.getId()).collection("product");
                                    userProducts.
                                            whereEqualTo("nome", userProduct.getNome())
                                            .whereEqualTo("marca", userProduct.getMarca())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            userProducts.document(document.getId()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Log.d("ACTION: ", "DONE");
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
        }
    }

 */



    private Prodotto trasformCollectionProduct(QueryDocumentSnapshot productDocument) {
        Prodotto tempProduct = null;
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
        return tempProduct;
    }

    interface OnProductAddition {
        void onSuccess() throws IOException;
    }

}

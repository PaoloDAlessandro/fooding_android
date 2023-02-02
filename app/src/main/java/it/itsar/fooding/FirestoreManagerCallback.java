package it.itsar.fooding;

import java.io.IOException;
import java.util.ArrayList;

public interface FirestoreManagerCallback {
    void onFinish(ArrayList<Prodotto> productFromCollection);

}

package it.itsar.fooding;

import java.io.IOException;
import java.util.ArrayList;

public interface FirestoreManagerCallback {
    public void onFinish(ArrayList<Prodotto> productFromCollection);

}

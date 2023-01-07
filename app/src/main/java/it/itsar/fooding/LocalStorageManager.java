package it.itsar.fooding;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class LocalStorageManager {

    private MyProperties myProperties = MyProperties.getInstance();

    public LocalStorageManager() {
    }

    public void writeObjectFile(File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(myProperties.getUserProdotti().toArray(new Prodotto[0]));
            oos.flush();
        }
    }

    public void leggi(String fileName) throws IOException {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Prodotto[] prodotti = (Prodotto[]) objectIn.readObject();
            myProperties.setUserProdotti(new ArrayList<>(Arrays.asList(prodotti)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

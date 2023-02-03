package it.itsar.fooding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Arrays;

public class LocalStorageManager {

    public static String USER_PRODUCT_FILE_NAME = "/storage.txt";
    public static String ULTIME_AGGIUNTE_FILE_NAME = "/ultime_aggiunte.txt";
    public static String RICETTE_CONSIGLIATE_FILE_NAME = "/ricette_consigliate.txt";
    public static String RICETTE_FILE_NAME = "/ricette.txt";
    private final MyProperties myProperties = MyProperties.getInstance();

    public LocalStorageManager() {
    }

    public void backupToFile(File file, ArrayList<Prodotto> prodotti) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(prodotti.toArray(new Prodotto[0]));
            oos.flush();
        }
    }

    public void backupToRecipesFile(File file, ArrayList<Ricetta> ricette) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(ricette.toArray(new Ricetta[0]));
            oos.flush();
        }
    }

    public ArrayList<Ricetta> backupFromRecipesFile(String fileName) throws IOException {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Ricetta[] ricette = (Ricetta[]) objectIn.readObject();
            return new ArrayList<>(Arrays.asList(ricette));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ArrayList<Prodotto> backupFromFile(String fileName) throws IOException {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Prodotto[] prodotti = (Prodotto[]) objectIn.readObject();
            return new ArrayList<>(Arrays.asList(prodotti));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean deleteFile(File file) {
        return file.delete();
    }

}

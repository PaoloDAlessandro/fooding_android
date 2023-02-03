package it.itsar.fooding;

import org.checkerframework.checker.units.qual.A;

import java.sql.Array;
import java.util.ArrayList;

public class MyProperties {
    private static MyProperties mInstance= null;

    private Prodotto[] prodotti;
    private ArrayList<Prodotto> userProdotti = new ArrayList<>();
    private ArrayList<Prodotto> ultimeAggiunte = new ArrayList<>();
    private ArrayList<Ricetta> ricetteConsigliate = new ArrayList<>();
    private ArrayList<Ricetta> ricette = new ArrayList<>();


    private String userUsername;

    protected MyProperties(){}

    public static synchronized MyProperties getInstance() {
        if(null == mInstance){
            mInstance = new MyProperties();
        }
        return mInstance;
    }

    public Prodotto[] getProdotti() {
        return prodotti;
    }

    public void setProdotti(Prodotto[] prodotti) {
        this.prodotti = prodotti;
    }

    public void addUserProduct(Prodotto prodotto) {
        this.userProdotti.add(prodotto);
    }

    public void setUserProdotti(ArrayList<Prodotto> userProdotti) {
        this.userProdotti = userProdotti;
    }

    public void removeProduct() {
        this.userProdotti.removeIf(prodotto -> prodotto.getDateScadenza().stream().allMatch(productExpirationDate -> productExpirationDate.getAmount() == 0));
    }

    public ArrayList<Prodotto> getUserProdotti() {
        return userProdotti;
    }

    public ArrayList<Prodotto> getUltimeAggiunte() {
        return ultimeAggiunte;
    }

    public void setUltimeAggiunte(ArrayList<Prodotto> ultimeAggiunte) {
        this.ultimeAggiunte = ultimeAggiunte;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public ArrayList<Ricetta> getRicette() {
        return ricette;
    }

    public void setRicette(ArrayList<Ricetta> ricette) {
        this.ricette = ricette;
    }

    public ArrayList<Ricetta> getRicetteConsigliate() {
        return ricetteConsigliate;
    }

    public void setRicetteConsigliate(ArrayList<Ricetta> ricetteConsigliate) {
        this.ricetteConsigliate = ricetteConsigliate;
    }
}

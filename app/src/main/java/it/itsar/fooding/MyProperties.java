package it.itsar.fooding;

import java.util.ArrayList;

public class MyProperties {
    private static MyProperties mInstance= null;

    private Prodotto[] prodotti;
    private ArrayList<Prodotto> userProdotti;

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

    public ArrayList<Prodotto> getUserProdotti() {
        return userProdotti;
    }
}

package it.itsar.fooding;

import java.io.Serializable;

public class Ingrediente implements Serializable {
    private int quantità;
    private Prodotto prodotto;

    public Ingrediente(int quantità, Prodotto prodotto) {
        this.quantità = quantità;
        this.prodotto = prodotto;
    }

    public int getQuantità() {
        return quantità;
    }

    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }
}

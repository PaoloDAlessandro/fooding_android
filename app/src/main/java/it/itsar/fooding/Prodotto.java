package it.itsar.fooding;

import android.os.Build;

import java.io.Serializable;
import java.util.ArrayList;

public class Prodotto implements Serializable {
    private String nome;
    private String marca;
    private String ingredienti;
    private int peso;
    private int preparazione;
    private ArrayList<ProductExpirationDate> dateScadenza;
    private int image;
    private int colore;
    private ValoriNutrizionali valoriNutrizionali;

    public Prodotto(String nome, String marca, String ingredienti, int peso, int preparazione, ArrayList<ProductExpirationDate> dateScadenza, int image, int colore, ValoriNutrizionali valoriNutrizionali) {
        this.nome = nome;
        this.marca = marca;
        this.ingredienti = ingredienti;
        this.peso = peso;
        this.preparazione = preparazione;
        this.dateScadenza = dateScadenza;
        this.image = image;
        this.colore = colore;
        this.valoriNutrizionali = valoriNutrizionali;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getPreparazione() {
        return preparazione;
    }

    public void setPreparazione(int preparazione) {
        this.preparazione = preparazione;
    }

    @Override
    public String toString() {
        return getNome();
    }

    public String getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(String ingredienti) {
        this.ingredienti = ingredienti;
    }

    public ValoriNutrizionali getValoriNutrizionali() {
        return valoriNutrizionali;
    }

    public void setValoriNutrizionali(ValoriNutrizionali valoriNutrizionali) {
        this.valoriNutrizionali = valoriNutrizionali;
    }

    public int getColore() {
        return colore;
    }

    public void setColore(int colore) {
        this.colore = colore;
    }


    public ArrayList<ProductExpirationDate> getDateScadenza() {
        return dateScadenza;
    }

    public void setDateScadenza(ArrayList<ProductExpirationDate> dateScadenza) {
        this.dateScadenza = dateScadenza;
    }

    public void addExpirationDate(ProductExpirationDate productExpirationDate) {
        dateScadenza.add(productExpirationDate);
    }

    public ProductExpirationDate getCloserExpirationdate() {
        getDateScadenza().sort((productExpirationDate1, productExpirationDate2) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return productExpirationDate1.getExpirationDate().compareTo(productExpirationDate2.getExpirationDate());
            }
            return 0;
        }
        );

        return getDateScadenza().get(0);
    }

    public int getAmountOfUnits() {
        int total = 0;
        for (ProductExpirationDate product:this.getDateScadenza()) {
            total += product.getAmount();
        }
        return total;
    }
}

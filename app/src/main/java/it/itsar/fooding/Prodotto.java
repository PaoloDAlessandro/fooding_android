package it.itsar.fooding;

import android.os.Build;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Prodotto implements Serializable {
    private String nome;
    private String marca;
    private String ingredienti;
    private int peso;
    private String unità;
    private int preparazione;
    private ArrayList<ProductExpirationDate> dateScadenza;
    private String image;
    private String colore;
    private ValoriNutrizionali valoriNutrizionali;
    private LocalDateTime dataAggiunta;

    public Prodotto(String nome, String marca, String ingredienti, int peso, String unità, int preparazione, ArrayList<ProductExpirationDate> dateScadenza, String image, String colore, ValoriNutrizionali valoriNutrizionali, LocalDateTime dataAggiunta) {
        this.nome = nome;
        this.marca = marca;
        this.ingredienti = ingredienti;
        this.peso = peso;
        this.unità = unità;
        this.preparazione = preparazione;
        this.dateScadenza = dateScadenza;
        this.image = image;
        this.colore = colore;
        this.valoriNutrizionali = valoriNutrizionali;
        this.dataAggiunta = dataAggiunta;
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

    public int getPreparazione() {
        return preparazione;
    }

    public void setPreparazione(int preparazione) {
        this.preparazione = preparazione;
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

    public void checkEmptyExpirationDate() {
        this.getDateScadenza().removeIf(productExpirationDate -> productExpirationDate.getAmount() == 0);
    }

    public String getUnità() {
        return unità;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    public LocalDateTime getDataAggiunta() {
        return dataAggiunta;
    }

    public void setUnità(String unità) {
        this.unità = unità;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public void setDataAggiunta(LocalDateTime dataAggiunta) {
        this.dataAggiunta = dataAggiunta;
    }

    public String toStringSecondary() {
        return "Prodotto{" +
                "nome='" + nome + '\'' +
                ", marca='" + marca + '\'' +
                ", ingredienti='" + ingredienti + '\'' +
                ", peso=" + peso +
                ", unità='" + unità + '\'' +
                ", preparazione=" + preparazione +
                ", dateScadenza=" + dateScadenza +
                ", image='" + image + '\'' +
                ", colore='" + colore + '\'' +
                ", valoriNutrizionali=" + valoriNutrizionali +
                ", dataAggiunta=" + dataAggiunta +
                '}';
    }

    public static String makeComparable(ArrayList<Prodotto> prodotti) {
        String result = "";
        for (Prodotto prodotto:prodotti) {
            result += prodotto.toStringSecondary();
        }
        return result;
    }
}

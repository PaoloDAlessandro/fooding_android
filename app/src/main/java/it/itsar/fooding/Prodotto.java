package it.itsar.fooding;

import java.io.Serializable;
import java.time.LocalDate;

public class Prodotto implements Serializable {
    private String nome;
    private String marca;
    private String ingredienti;
    private int peso;
    private int giacenza;
    private int preparazione;
    private LocalDate dataScadenza;
    private int image;
    private int colore;
    private ValoriNutrizionali valoriNutrizionali;

    public Prodotto(String nome, String marca, String ingredienti, int peso, int giacenza, int preparazione, LocalDate dataScadenza, int image, int colore, ValoriNutrizionali valoriNutrizionali) {
        this.nome = nome;
        this.marca = marca;
        this.ingredienti = ingredienti;
        this.peso = peso;
        this.giacenza = giacenza;
        this.preparazione = preparazione;
        this.dataScadenza = dataScadenza;
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

    public int getGiacenza() {
        return giacenza;
    }

    public void setGiacenza(int giacenza) {
        this.giacenza = giacenza;
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
        return "Prodotto{" +
                "nome='" + nome + '\'' +
                ", marca='" + marca + '\'' +
                ", peso=" + peso +
                ", giacenza=" + giacenza +
                ", preparazione=" + preparazione +
                ", image=" + image +
                '}';
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

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
}

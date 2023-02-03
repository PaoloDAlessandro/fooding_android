package it.itsar.fooding;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;

public class Ricetta implements Serializable {
    private String nome;
    private String image;
    private String autore;
    private ArrayList<Ingrediente> ingredienti;
    private int tempoPreparazione;
    private int kcal;
    private Difficolta difficolta;

    public Ricetta(String nome, String image, String autore, ArrayList<Ingrediente> ingredienti, int tempoPreparazione, int kcal, Difficolta difficolta) {
        this.nome = nome;
        this.image = image;
        this.autore = autore;
        this.ingredienti = ingredienti;
        this.tempoPreparazione = tempoPreparazione;
        this.kcal = kcal;
        this.difficolta = difficolta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public int getTempoPreparazione() {
        return tempoPreparazione;
    }

    public void setTempoPreparazione(int tempoPreparazione) {
        this.tempoPreparazione = tempoPreparazione;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public Difficolta getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    public ArrayList<Ingrediente> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(ArrayList<Ingrediente> ingredienti) {
        this.ingredienti = ingredienti;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Ricetta{" +
                "nome='" + nome + '\'' +
                ", image='" + image + '\'' +
                ", autore='" + autore + '\'' +
                ", ingredienti=" + ingredienti +
                ", tempoPreparazione=" + tempoPreparazione +
                ", kcal=" + kcal +
                ", difficolta=" + difficolta +
                '}';
    }

    enum Difficolta {
        FACILE,
        MEDIA,
        DIFFICILE
    }
}

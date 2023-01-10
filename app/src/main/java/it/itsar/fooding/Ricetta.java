package it.itsar.fooding;

public class Ricetta {
    private String nome;
    private int image;
    private String autore;
    private int tempoPreparazione;
    private int kcal;
    private Difficolta difficolta;

    public Ricetta(String nome, int image, String autore, int tempoPreparazione, int kcal, Difficolta difficolta) {
        this.nome = nome;
        this.image = image;
        this.autore = autore;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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

    enum Difficolta {
        FACILE,
        MEDIA,
        DIFFICILE
    }

}

package it.itsar.fooding;

public class Prodotto {
    private String nome;
    private String marca;
    private int peso;
    private int giacenza;
    private int preparazione;
    private int image;


    public Prodotto(String nome, String marca, int peso, int giacenza, int preparazione, int image) {
        this.nome = nome;
        this.marca = marca;
        this.peso = peso;
        this.giacenza = giacenza;
        this.preparazione = preparazione;
        this.image = image;
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
}

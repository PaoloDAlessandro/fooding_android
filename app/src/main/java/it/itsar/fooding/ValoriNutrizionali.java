package it.itsar.fooding;

import java.io.Serializable;

public class ValoriNutrizionali implements Serializable {
    private double energia; //espressa in kj e convertita in kcal tramite convertToKcal() method
    private int energiaAR;
    private double grassi;
    private int grassiAR;
    private double grassiSaturi;
    private int grassiSaturiAR;
    private double carboidrati;
    private int carboidratiAR;
    private double zuccheri;
    private int zuccheriAR;
    private double fibre;
    private int fibreAR;
    private double proteine;
    private int proteineAR;
    private double sale;
    private int saleAR;


    public ValoriNutrizionali(double energia, int energiaAR, double grassi, int grassiAR, double grassiSaturi, int grassiSaturiAR, double carboidrati, int carboidratiAR, double zuccheri, int zuccheriAR, double fibre, int fibreAR, double proteine, int proteineAR, double sale, int saleAR) {
        this.energia = energia;
        this.energiaAR = energiaAR;
        this.grassi = grassi;
        this.grassiAR = grassiAR;
        this.grassiSaturi = grassiSaturi;
        this.grassiSaturiAR = grassiSaturiAR;
        this.carboidrati = carboidrati;
        this.carboidratiAR = carboidratiAR;
        this.zuccheri = zuccheri;
        this.zuccheriAR = zuccheriAR;
        this.fibre = fibre;
        this.fibreAR = fibreAR;
        this.proteine = proteine;
        this.proteineAR = proteineAR;
        this.sale = sale;
        this.saleAR = saleAR;
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        this.energia = energia;
    }

    public int getEnergiaAR() {
        return energiaAR;
    }

    public void setEnergiaAR(int energiaAR) {
        this.energiaAR = energiaAR;
    }

    public double getGrassi() {
        return grassi;
    }

    public void setGrassi(double grassi) {
        this.grassi = grassi;
    }

    public int getGrassiAR() {
        return grassiAR;
    }

    public void setGrassiAR(int grassiAR) {
        this.grassiAR = grassiAR;
    }

    public double getGrassiSaturi() {
        return grassiSaturi;
    }

    public void setGrassiSaturi(double grassiSaturi) {
        this.grassiSaturi = grassiSaturi;
    }

    public int getGrassiSaturiAR() {
        return grassiSaturiAR;
    }

    public void setGrassiSaturiAR(int grassiSaturiAR) {
        this.grassiSaturiAR = grassiSaturiAR;
    }

    public double getCarboidrati() {
        return carboidrati;
    }

    public void setCarboidrati(double carboidrati) {
        this.carboidrati = carboidrati;
    }

    public int getCarboidratiAR() {
        return carboidratiAR;
    }

    public void setCarboidratiAR(int carboidratiAR) {
        this.carboidratiAR = carboidratiAR;
    }

    public double getZuccheri() {
        return zuccheri;
    }

    public void setZuccheri(double zuccheri) {
        this.zuccheri = zuccheri;
    }

    public double getFibre() {
        return fibre;
    }

    public void setFibre(double fibre) {
        this.fibre = fibre;
    }

    public int getFibreAR() {
        return fibreAR;
    }

    public void setFibreAR(int fibreAR) {
        this.fibreAR = fibreAR;
    }

    public double getProteine() {
        return proteine;
    }

    public void setProteine(double proteine) {
        this.proteine = proteine;
    }

    public int getProteineAR() {
        return proteineAR;
    }

    public void setProteineAR(int proteineAR) {
        this.proteineAR = proteineAR;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public int getSaleAR() {
        return saleAR;
    }

    public void setSaleAR(int saleAR) {
        this.saleAR = saleAR;
    }

    public int getZuccheriAR() {
        return zuccheriAR;
    }

    public void setZuccheriAR(int zuccheriAR) {
        this.zuccheriAR = zuccheriAR;
    }

    public int convertToKcal() {
        return (int) ((int) energia / 4.184);
    }
}

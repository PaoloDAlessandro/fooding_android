package it.itsar.fooding;

import java.io.Serializable;

public class ValoriNutrizionali implements Serializable {
    private double energia; //espressa in kj e convertita in kcal tramite convertToKcal() method
    private double energiaAR;
    private double grassi;
    private double grassiAR;
    private double grassiSaturi;
    private double grassiSaturiAR;
    private double carboidrati;
    private double carboidratiAR;
    private double zuccheri;
    private double zuccheriAR;
    private double fibre;
    private double fibreAR;
    private double proteine;
    private double proteineAR;
    private double sale;
    private double saleAR;


    public ValoriNutrizionali(double energia, double energiaAR, double grassi, double grassiAR, double grassiSaturi, double grassiSaturiAR, double carboidrati, double carboidratiAR, double zuccheri, double zuccheriAR, double fibre, double fibreAR, double proteine, double proteineAR, double sale, double saleAR) {
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


    public int convertToKcal() {
        return (int) ((int) energia / 4.184);
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        this.energia = energia;
    }

    public double getEnergiaAR() {
        return energiaAR;
    }

    public void setEnergiaAR(double energiaAR) {
        this.energiaAR = energiaAR;
    }

    public double getGrassi() {
        return grassi;
    }

    public void setGrassi(double grassi) {
        this.grassi = grassi;
    }

    public double getGrassiAR() {
        return grassiAR;
    }

    public void setGrassiAR(double grassiAR) {
        this.grassiAR = grassiAR;
    }

    public double getGrassiSaturi() {
        return grassiSaturi;
    }

    public void setGrassiSaturi(double grassiSaturi) {
        this.grassiSaturi = grassiSaturi;
    }

    public double getGrassiSaturiAR() {
        return grassiSaturiAR;
    }

    public void setGrassiSaturiAR(double grassiSaturiAR) {
        this.grassiSaturiAR = grassiSaturiAR;
    }

    public double getCarboidrati() {
        return carboidrati;
    }

    public void setCarboidrati(double carboidrati) {
        this.carboidrati = carboidrati;
    }

    public double getCarboidratiAR() {
        return carboidratiAR;
    }

    public void setCarboidratiAR(double carboidratiAR) {
        this.carboidratiAR = carboidratiAR;
    }

    public double getZuccheri() {
        return zuccheri;
    }

    public void setZuccheri(double zuccheri) {
        this.zuccheri = zuccheri;
    }

    public double getZuccheriAR() {
        return zuccheriAR;
    }

    public void setZuccheriAR(double zuccheriAR) {
        this.zuccheriAR = zuccheriAR;
    }

    public double getFibre() {
        return fibre;
    }

    public void setFibre(double fibre) {
        this.fibre = fibre;
    }

    public double getFibreAR() {
        return fibreAR;
    }

    public void setFibreAR(double fibreAR) {
        this.fibreAR = fibreAR;
    }

    public double getProteine() {
        return proteine;
    }

    public void setProteine(double proteine) {
        this.proteine = proteine;
    }

    public double getProteineAR() {
        return proteineAR;
    }

    public void setProteineAR(double proteineAR) {
        this.proteineAR = proteineAR;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getSaleAR() {
        return saleAR;
    }

    public void setSaleAR(double saleAR) {
        this.saleAR = saleAR;
    }
}




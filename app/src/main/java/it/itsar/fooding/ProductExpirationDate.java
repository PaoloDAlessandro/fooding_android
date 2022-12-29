package it.itsar.fooding;

import java.time.LocalDate;

public class ProductExpirationDate {
    private int amount;
    private LocalDate expirationDate;

    public ProductExpirationDate(int amount, LocalDate expirationDate) {
        this.amount = amount;
        this.expirationDate = expirationDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}

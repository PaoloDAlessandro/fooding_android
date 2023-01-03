package it.itsar.fooding;

import java.io.Serializable;
import java.time.LocalDate;

public class ProductExpirationDate implements Serializable, Cloneable {
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

    @Override
    public String toString() {
        return "ProductExpirationDate{" +
                "amount=" + amount +
                ", expirationDate=" + expirationDate +
                '}';
    }

    @Override
    public ProductExpirationDate clone() {
        try {
            ProductExpirationDate clone = (ProductExpirationDate) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

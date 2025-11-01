package iuh.fit.se.group1.entity;

import java.time.LocalDate;

public class SurchargeDetail {

    private Surcharge surcharge;
    private int quantity;
    private LocalDate createdAt;

    public SurchargeDetail() {
    }

    public SurchargeDetail(Surcharge surcharge, int quantity, LocalDate createdAt) {
        this.surcharge = surcharge;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public SurchargeDetail(Surcharge surcharge, int quantity) {
        this.surcharge = surcharge;
        this.quantity = quantity;
    }

    public Surcharge getSurcharge() {
        return surcharge;
    }


    public void setSurcharge(Surcharge surcharge) {
        this.surcharge = surcharge;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }


}

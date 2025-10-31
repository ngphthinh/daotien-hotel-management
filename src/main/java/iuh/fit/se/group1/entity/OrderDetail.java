package iuh.fit.se.group1.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class OrderDetail {
    private BigDecimal unitPrice;
    private Amenity amenity;
    private int quantity;
    private LocalDate createdAt;


    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Amenity getAmenity() {
        return amenity;
    }

    public void setAmenity(Amenity amenity) {
        this.amenity = amenity;
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

    public OrderDetail(BigDecimal unitPrice, Amenity amenity, int quantity, LocalDate createdAt) {
        this.unitPrice = unitPrice;
        this.amenity = amenity;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public OrderDetail(BigDecimal unitPrice, Amenity amenity, int quantity) {
        this.unitPrice = unitPrice;
        this.amenity = amenity;
        this.quantity = quantity;
        this.createdAt = LocalDate.now();
    }

    public OrderDetail() {
    }

    public void calcUnitPrice() {
        if (amenity != null && amenity.getPrice() != null) {
            this.unitPrice = amenity.getPrice().multiply(BigDecimal.valueOf(quantity));
        } else {
            this.unitPrice = BigDecimal.ZERO;
        }
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                ", unitPrice=" + unitPrice +
                ", amenity=" + amenity +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                '}';
    }


}

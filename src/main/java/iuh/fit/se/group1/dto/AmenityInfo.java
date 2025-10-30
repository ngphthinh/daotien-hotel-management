package iuh.fit.se.group1.dto;

import java.math.BigDecimal;

public class AmenityInfo {
    private int quantity;
    private BigDecimal price;

    public AmenityInfo(int quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AmenityInfo{" +
                "quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}

package iuh.fit.se.group1.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Surcharge {
    private long surchargeId;
    private String name;
    private BigDecimal price;
    private long orderId;       
    private LocalDate createdAt;

    public Surcharge(long surchargeId, String name, BigDecimal price, long orderId, LocalDate createdAt) {
        this.surchargeId = surchargeId;
        this.name = name;
        this.price = price;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }

    public Surcharge(String name, BigDecimal price, long orderId) {
        this.name = name;
        this.price = price;
        this.orderId = orderId;
    }

    public Surcharge() {
    }

    public long getSurchargeId() {
        return surchargeId;
    }

    public void setSurchargeId(long surchargeId) {
        this.surchargeId = surchargeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.surchargeId ^ (this.surchargeId >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Surcharge other = (Surcharge) obj;
        return this.surchargeId == other.surchargeId;
    }

    @Override
    public String toString() {
        return "Surcharge{" +
                "surchargeId=" + surchargeId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", orderId=" + orderId +
                ", createdAt=" + createdAt +
                '}';
    }
}

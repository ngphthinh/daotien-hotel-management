package iuh.fit.se.group1.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class OrderDetail {
    private Long orderDetailId;
    private BigDecimal unitPrice;
    private Amenity amenity;
    private int quantity;
    private LocalDate createdAt;

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

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

    public OrderDetail(Long orderDetailId, BigDecimal unitPrice, Amenity amenity, int quantity, LocalDate createdAt) {
        this.orderDetailId = orderDetailId;
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

    public BigDecimal calcUnitPrice() {
        //todo:
        return null;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderDetailId=" + orderDetailId +
                ", unitPrice=" + unitPrice +
                ", amenity=" + amenity +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrderDetail that)) return false;
        return Objects.equals(orderDetailId, that.orderDetailId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderDetailId);
    }
}

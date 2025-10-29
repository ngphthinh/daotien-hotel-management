package iuh.fit.se.group1.entity;

import java.time.LocalDate;
import java.util.Objects;

public class OrderType {
    private Long orderTypeId;
    private String name;
    private LocalDate createdAt;

    public OrderType(Long orderTypeId, String name, LocalDate createdAt) {
        this.orderTypeId = orderTypeId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public OrderType() {
    }

    public OrderType(String name) {
        this.name = name;
        this.createdAt = LocalDate.now();
    }

    public OrderType(Long orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public Long getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(Long orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrderType orderType)) return false;
        return Objects.equals(orderTypeId, orderType.orderTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderTypeId);
    }

    @Override
    public String toString() {
        return "OrderType{" +
                "orderTypeId=" + orderTypeId +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

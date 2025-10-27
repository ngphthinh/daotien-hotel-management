package iuh.fit.se.group1.entity;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private Employee employee;
    private OrderType orderType;
    private Promotion promotion;
    private LocalDate createdAt;

    public Order() {
    }

    public Order(Long orderId, LocalDateTime orderDate, BigDecimal totalAmount, Employee employee, OrderType orderType, Promotion promotion, LocalDate createdAt) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.employee = employee;
        this.orderType = orderType;
        this.promotion = promotion;
        this.createdAt = createdAt;
    }

    public Order(BigDecimal totalAmount, Employee employee, OrderType orderType, Promotion promotion) {
        this.totalAmount = totalAmount;
        this.employee = employee;
        this.orderType = orderType;
        this.promotion = promotion;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }


    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", employee=" + employee +
                ", orderType=" + orderType +
                ", promotion=" + promotion +
                ", createdAt=" + createdAt +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId);
    }
}

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
    private Booking booking;
    private List<Surcharge> surcharges;
    private List<OrderDetail> orderDetails;
    private Payment payment;
    private LocalDate createdAt;

    public Order() {
    }

    public Order(Long orderId, LocalDateTime orderDate, BigDecimal totalAmount, Employee employee, OrderType orderType, Promotion promotion, Booking booking, List<Surcharge> surcharges, List<OrderDetail> orderDetails, Payment payment, LocalDate createdAt) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.employee = employee;
        this.orderType = orderType;
        this.promotion = promotion;
        this.booking = booking;
        this.surcharges = surcharges;
        this.orderDetails = orderDetails;
        this.payment = payment;
        this.createdAt = createdAt;
    }

    public Order(BigDecimal totalAmount, Employee employee, OrderType orderType, Promotion promotion, Booking booking, Payment payment) {
        this.totalAmount = totalAmount;
        this.employee = employee;
        this.orderType = orderType;
        this.promotion = promotion;
        this.booking = booking;
        this.payment = payment;
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

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public List<Surcharge> getSurcharges() {
        return surcharges;
    }

    public void setSurcharges(List<Surcharge> surcharges) {
        this.surcharges = surcharges;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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
                ", booking=" + booking +
                ", surcharges=" + surcharges +
                ", orderDetails=" + orderDetails +
                ", payment=" + payment +
                ", createdAt=" + createdAt +
                '}';
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        if (this.orderDetails == null) {
            this.orderDetails = new java.util.ArrayList<>();
        }
        if (orderDetail == null) {
            throw new IllegalArgumentException("OrderDetail cannot be null");
        }
        this.orderDetails.add(orderDetail);
    }

    public void addSurcharge(Surcharge surcharge) {
        if (this.surcharges == null) {
            this.surcharges = new java.util.ArrayList<>();
        }
        if (surcharge == null) {
            throw new IllegalArgumentException("Surcharge cannot be null");
        }
        this.surcharges.add(surcharge);
    }

    public void removeOrderDetail(OrderDetail orderDetail) {
        if (this.orderDetails != null && orderDetail != null) {
            this.orderDetails.remove(orderDetail);
        }
    }
    public void removeSurcharge(Surcharge surcharge) {
        if (this.surcharges != null && surcharge != null) {
            this.surcharges.remove(surcharge);
        }
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

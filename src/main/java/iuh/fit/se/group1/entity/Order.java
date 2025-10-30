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
    private Customer customer;
    private Promotion promotion;
    private BigDecimal deposit;
    private LocalDate createdAt;
    private List<Booking> bookings;

    public Order() {
        bookings = new java.util.ArrayList<>();
    }

    public Order(Long orderId, LocalDateTime orderDate, BigDecimal totalAmount, Employee employee, OrderType orderType, Customer customer, Promotion promotion, BigDecimal deposit, LocalDate createdAt, List<Booking> bookings) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.employee = employee;
        this.orderType = orderType;
        this.customer = customer;
        this.promotion = promotion;
        this.deposit = deposit;
        this.createdAt = createdAt;
        this.bookings = bookings;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
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
                ", orderType=" + orderType.getOrderTypeId() +
                ", customer=" + customer.getCustomerId() +
                ", promotion=" + promotion +
                ", deposit=" + deposit +
                ", createdAt=" + createdAt +
                ", bookings=" + bookings +
                '}';
    }

    public void addBooking(Booking booking) {
        booking.setOrder(this);
        this.bookings.add(booking);
    }

    public BigDecimal getTotalAmountBooking(){
        return bookings.stream().map(Booking::getTotalPrice).reduce(BigDecimal.ONE,BigDecimal::add);
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

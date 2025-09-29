package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.BookingType;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Booking {
    private Long bookingId;
    private Customer customer;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Employee employee;
    private BigDecimal deposit;
    private Room room;
    private BookingType bookingType;
    private Payment payment;
    private BigDecimal totalPrice;
    private LocalDate createdAt;

    public Booking() {
    }

    public Booking(Long bookingId, Customer customer, LocalDateTime checkInDate, LocalDateTime checkOutDate, Employee employee, BigDecimal deposit, Room room, BookingType bookingType, Payment payment, BigDecimal totalPrice, LocalDate createdAt) {
        this.setBookingId(bookingId);
        this.setCustomer(customer);
        this.setCheckInDate(checkInDate);
        this.setCheckOutDate(checkOutDate);
        this.setEmployee(employee);
        this.setDeposit(deposit);
        this.setRoom(room);
        this.setBookingType(bookingType);
        this.setPayment(payment);
        this.setTotalPrice(calcTotalPrice());
        this.setCreatedAt(createdAt);
    }

    public Booking(Customer customer, LocalDateTime checkInDate, LocalDateTime checkOutDate, Employee employee, BigDecimal deposit, Room room, BookingType bookingType) {
        this.setCustomer(customer);
        this.setCheckInDate(checkInDate);
        this.setCheckOutDate(checkOutDate);
        this.setEmployee(employee);
        this.setDeposit(deposit);
        this.setRoom(room);
        this.setBookingType(bookingType);
        this.setTotalPrice(calcTotalPrice());
        this.setCreatedAt(LocalDate.now());
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDateTime checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDateTime checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public BookingType getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingType bookingType) {
        this.bookingType = bookingType;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Booking booking)) return false;
        return Objects.equals(bookingId, booking.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookingId);
    }

    public BigDecimal calcTotalPrice() {
        //todo
        return null;
    }

    private boolean isHoliday(LocalDate date) {
        // TODO: kiểm tra ngày lễ
        return false;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", customer=" + customer +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", employee=" + employee +
                ", deposit=" + deposit +
                ", room=" + room +
                ", bookingType='" + bookingType + '\'' +
                ", payment=" + payment +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                '}';
    }
}

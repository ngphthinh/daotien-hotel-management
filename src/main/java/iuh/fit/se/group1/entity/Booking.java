package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.BookingType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Booking {
    private Long bookingId;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Employee employee;
    private Order order;
    private Room room;
    private BookingType bookingType;
    private BigDecimal totalPrice;
    private LocalDate createdAt;


    public Booking(Order order) {
        this.order = order;
    }

    public Booking(Long bookingId, LocalDateTime checkInDate, LocalDateTime checkOutDate, Employee employee, Room room, BookingType bookingType, BigDecimal totalPrice, LocalDate createdAt) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.employee = employee;
        this.room = room;
        this.bookingType = bookingType;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public Booking(LocalDateTime checkInDate, LocalDateTime checkOutDate, Employee employee, Room room, BookingType bookingType, Order order) {
        this.order = order;
        this.setCheckInDate(checkInDate);
        this.setCheckOutDate(checkOutDate);
        this.setEmployee(employee);
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
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", employee=" + employee +
                ", room=" + room +
                ", bookingType=" + bookingType +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                '}';
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.BookingType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

public class Booking {
    private Long bookingId;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private BookingType bookingType;
    private Order order;
    private Room room;
    private LocalDate createdAt;


    public Booking(Order order) {
        this.order = order;
    }


    public Booking(LocalDateTime checkInDate, LocalDateTime checkOutDate,Room room, BookingType bookingType, Order order) {
        this.order = order;
        this.setCheckInDate(checkInDate);
        this.setCheckOutDate(checkOutDate);
        this.setRoom(room);
        this.setBookingType(bookingType);
        this.setCreatedAt(LocalDate.now());
    }

    public Booking() {

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


    public boolean isHoliday(LocalDate startDate, LocalDate endDate) {
        Set<String> FIXED_HOLIDAYS = Set.of(
                "01-01", // Tết Dương lịch
                "30-04", // Giải phóng miền Nam
                "01-05", // Quốc tế Lao động
                "02-09"  // Quốc khánh
        );

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            // Kiểm tra ngày dương lịch
            String key = String.format("%02d-%02d", date.getDayOfMonth(), date.getMonthValue());
            if (FIXED_HOLIDAYS.contains(key)) return true;

            // Kiểm tra ngày âm lịch
//            ChineseDate lunar = new ChineseDate(java.sql.Date.valueOf(date));
//            int lunarDay = lunar.getDay();
//            int lunarMonth = lunar.getMonth();
//
//            // Giỗ Tổ (10/3 AL)
//            if (lunarMonth == 3 && lunarDay == 10) return true;
//
//            // Tết Nguyên Đán (29,30 tháng Chạp + 1–5 tháng Giêng)
//            if ((lunarMonth == 12 && (lunarDay == 29 || lunarDay == 30)) ||
//                    (lunarMonth == 1 && lunarDay <= 5)) return true;

            date = date.plusDays(1);
        }
        return false;
    }


    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", room=" + room +
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

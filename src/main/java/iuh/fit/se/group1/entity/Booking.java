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

    public void calcTotalPrice(String roomTypeIndex) {
        if (room == null || checkInDate == null || checkOutDate == null || bookingType == null) {
            throw new IllegalStateException("Room, check-in date, check-out date, and booking type must be set before calculating total price.");
        }

        // Kiểm tra hợp lệ
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }

        BigDecimal totalPrice;

        boolean isSingle = roomTypeIndex.equalsIgnoreCase("0");

        switch (bookingType) {
            case DAILY -> {
                BigDecimal rate = isSingle ? BigDecimal.valueOf(300_000) : BigDecimal.valueOf(500_000);
                long days = Math.max(1, java.time.Duration.between(checkInDate, checkOutDate).toDays());
                totalPrice = rate.multiply(BigDecimal.valueOf(days));
            }

            case HOURLY -> {
                BigDecimal baseRate = isSingle ? BigDecimal.valueOf(50_000) : BigDecimal.valueOf(80_000);
                BigDecimal hourlyRate = isSingle ? BigDecimal.valueOf(20_000) : BigDecimal.valueOf(30_000);
                long hours = java.time.Duration.between(checkInDate, checkOutDate).toHours();
                if (hours < 1) hours = 1;
                totalPrice = baseRate.add(hourlyRate.multiply(BigDecimal.valueOf(hours - 1)));
            }

            case OVERNIGHT -> {
                totalPrice = isSingle ? BigDecimal.valueOf(250_000) : BigDecimal.valueOf(350_000);
            }

            default -> throw new IllegalArgumentException("Unknown booking type: " + bookingType);
        }
        this.totalPrice = totalPrice;
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


    public boolean isSurchargeCheckOut(){
        switch (bookingType) {
            case DAILY -> {
                return LocalDateTime.now().isAfter(LocalDateTime.of(checkOutDate.toLocalDate(), LocalTime.of(12, 0)));
            }
            case HOURLY -> {
                return LocalDateTime.now().isAfter(checkOutDate.plusMinutes(30));
            }
            case OVERNIGHT -> {
                return isHoliday(checkInDate.toLocalDate(), checkOutDate.toLocalDate());
            }
            default -> throw new IllegalArgumentException("Unknown booking type: " + bookingType);
        }
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

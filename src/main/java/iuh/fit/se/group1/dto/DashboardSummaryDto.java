package iuh.fit.se.group1.dto;

import java.math.BigDecimal;

/**
 * DTO chứa tổng hợp dữ liệu dashboard nhân viên
 */
public class DashboardSummaryDto {
    private int roomsNearExpiry;
    private int totalRooms;
    private int checkInCount;
    private int checkOutCount;
    private int bookingCount;  // Số lượt đặt phòng
    private BigDecimal openShiftCash;

    public DashboardSummaryDto() {
        this.openShiftCash = BigDecimal.ZERO;
    }

    public DashboardSummaryDto(int roomsNearExpiry, int totalRooms, int checkInCount,
                               int checkOutCount, BigDecimal openShiftCash) {
        this.roomsNearExpiry = roomsNearExpiry;
        this.totalRooms = totalRooms;
        this.checkInCount = checkInCount;
        this.checkOutCount = checkOutCount;
        this.bookingCount = 0;
        this.openShiftCash = openShiftCash != null ? openShiftCash : BigDecimal.ZERO;
    }

    public int getRoomsNearExpiry() {
        return roomsNearExpiry;
    }

    public void setRoomsNearExpiry(int roomsNearExpiry) {
        this.roomsNearExpiry = roomsNearExpiry;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public int getCheckInCount() {
        return checkInCount;
    }

    public void setCheckInCount(int checkInCount) {
        this.checkInCount = checkInCount;
    }

    public int getCheckOutCount() {
        return checkOutCount;
    }

    public void setCheckOutCount(int checkOutCount) {
        this.checkOutCount = checkOutCount;
    }

    public int getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(int bookingCount) {
        this.bookingCount = bookingCount;
    }

    public BigDecimal getOpenShiftCash() {
        return openShiftCash;
    }

    public void setOpenShiftCash(BigDecimal openShiftCash) {
        this.openShiftCash = openShiftCash != null ? openShiftCash : BigDecimal.ZERO;
    }
}


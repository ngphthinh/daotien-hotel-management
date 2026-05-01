package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO chứa tổng hợp dữ liệu dashboard nhân viên
 */
@Data
@Builder
@AllArgsConstructor
public class DashboardSummaryDto implements Serializable {
    private int roomsNearExpiry;
    private int totalRooms;
    private int checkInCount;
    private int checkOutCount;
    private int bookingCount;  // Số lượt đặt phòng
    private BigDecimal openShiftCash;

    public DashboardSummaryDto() {
        this.openShiftCash = BigDecimal.ZERO;
    }


    public void setOpenShiftCash(BigDecimal openShiftCash) {
        this.openShiftCash = openShiftCash != null ? openShiftCash : BigDecimal.ZERO;
    }
}


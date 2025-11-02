/**
 * @ (#) ShiftClose.java   1.0     31/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet 
 * @version: 1.0
 * @created: 31/10/2025
 */

public class ShiftClose {
    private Long shiftCloseId;
    private EmployeeShift employeeShift;
    private BigDecimal totalRevenue;
    private  BigDecimal cashInDrawer;
    private BigDecimal difference;
    private String note;
    private LocalDateTime createdAt;

    public ShiftClose() {
    }

    public ShiftClose(Long shiftCloseId, EmployeeShift employeeShift, BigDecimal totalRevenue, BigDecimal cashInDrawer, BigDecimal difference, String note, LocalDateTime createdAt) {
        this.shiftCloseId = shiftCloseId;
        this.employeeShift = employeeShift;
        this.totalRevenue = totalRevenue;
        this.cashInDrawer = cashInDrawer;
        this.difference = difference;
        this.note = note;
        this.createdAt = LocalDateTime.now();
    }

    public Long getShiftCloseId() {
        return shiftCloseId;
    }

    public void setShiftCloseId(Long shiftCloseId) {
        this.shiftCloseId = shiftCloseId;
    }

    public EmployeeShift getEmployeeShift() {
        return employeeShift;
    }

    public void setEmployeeShift(EmployeeShift employeeShift) {
        this.employeeShift = employeeShift;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getCashInDrawer() {
        return cashInDrawer;
    }

    public void setCashInDrawer(BigDecimal cashInDrawer) {
        this.cashInDrawer = cashInDrawer;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftClose that)) return false;
        return Objects.equals(shiftCloseId, that.shiftCloseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shiftCloseId);
    }

    @Override
    public String toString() {
        return "ShiftClose{" +
                "shiftCloseId=" + shiftCloseId +
                ", employeeShift=" + employeeShift +
                ", totalRevenue=" + totalRevenue +
                ", cashInDrawer=" + cashInDrawer +
                ", difference=" + difference +
                ", note='" + note + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

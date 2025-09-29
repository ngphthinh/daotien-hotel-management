package iuh.fit.se.group1.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class EmployeeShift {
    private	Long employeeShiftId;
    private	Employee employee;
    private	Shift shift;
    private LocalDateTime closingTime;
    private BigDecimal systemAmount;
    private	BigDecimal actualAmount;
    private	BigDecimal difference;
    private LocalDate createdAt;

    public EmployeeShift() {
    }

    public EmployeeShift(Long employeeShiftId, Employee employee, Shift shift, LocalDateTime closingTime, BigDecimal systemAmount, BigDecimal actualAmount, BigDecimal difference, LocalDate createdAt) {
        this.employeeShiftId = employeeShiftId;
        this.employee = employee;
        this.shift = shift;
        this.closingTime = closingTime;
        this.systemAmount = systemAmount;
        this.actualAmount = actualAmount;
        this.difference = difference;
        this.createdAt = createdAt;
    }

    public EmployeeShift(Employee employee, Shift shift, LocalDateTime closingTime) {
        this.employee = employee;
        this.shift = shift;
        this.closingTime = closingTime;
    }

    public Long getEmployeeShiftId() {
        return employeeShiftId;
    }

    public void setEmployeeShiftId(Long employeeShiftId) {
        this.employeeShiftId = employeeShiftId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalDateTime closingTime) {
        this.closingTime = closingTime;
    }

    public BigDecimal getSystemAmount() {
        return systemAmount;
    }

    public void setSystemAmount(BigDecimal systemAmount) {
        this.systemAmount = systemAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmployeeShift that)) return false;
        return Objects.equals(employeeShiftId, that.employeeShiftId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(employeeShiftId);
    }

    @Override
    public String toString() {
        return "EmployeeShift{" +
                "employeeShiftId=" + employeeShiftId +
                ", employee=" + employee +
                ", shift=" + shift +
                ", closingTime=" + closingTime +
                ", systemAmount=" + systemAmount +
                ", actualAmount=" + actualAmount +
                ", difference=" + difference +
                ", createdAt=" + createdAt +
                '}';
    }
}

package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.DenominationLabel;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;


public class DenominationDetail {
    private	Long denominationDetailId;
    private DenominationLabel denomination;
    private	int quantity;
    private EmployeeShift employeeShift;
    private LocalDate createdAt;

    public DenominationDetail() {
    }

    public DenominationDetail(Long denominationDetailId, DenominationLabel denomination, int quantity, EmployeeShift employeeShift, LocalDate createdAt) {
        this.denominationDetailId = denominationDetailId;
        this.denomination = denomination;
        this.quantity = quantity;
        this.employeeShift = employeeShift;
        this.createdAt = createdAt;
    }

    public DenominationDetail(DenominationLabel denomination, int quantity) {
        this.denomination = denomination;
        this.quantity = quantity;
        this.createdAt = LocalDate.now();
    }

    public Long getDenominationDetailId() {
        return denominationDetailId;
    }

    public void setDenominationDetailId(Long denominationDetailId) {
        this.denominationDetailId = denominationDetailId;
    }

    public DenominationLabel getDenomination() {
        return denomination;
    }

    public void setDenomination(DenominationLabel denomination) {
        this.denomination = denomination;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public EmployeeShift getEmployeeShift() {
        return employeeShift;
    }

    public void setEmployeeShift(EmployeeShift employeeShift) {
        this.employeeShift = employeeShift;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DenominationDetail that)) return false;
        return Objects.equals(denominationDetailId, that.denominationDetailId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(denominationDetailId);
    }

    @Override
    public String toString() {
        return "DenominationDetail{" +
                "denominationId=" + denominationDetailId +
                ", denomination=" + denomination +
                ", quantity=" + quantity +
                ", employeeShift=" + employeeShift +
                ", createdAt=" + createdAt +
                '}';
    }
}

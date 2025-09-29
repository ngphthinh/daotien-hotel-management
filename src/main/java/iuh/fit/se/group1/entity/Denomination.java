package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.DenominationLabel;

import java.time.LocalDate;
import java.util.Objects;

public class Denomination {
    private	Long id;
    private DenominationLabel denomination;
    private	int quantity;
    private LocalDate createdAt;

    public Denomination() {
    }

    public Denomination(Long id, DenominationLabel denomination, int quantity, LocalDate createdAt) {
        this.id = id;
        this.denomination = denomination;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public Denomination(DenominationLabel denomination, int quantity) {
        this.denomination = denomination;
        this.quantity = quantity;
        this.createdAt = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Denomination that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Denomination{" +
                "id=" + id +
                ", denomination=" + denomination +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                '}';
    }
}

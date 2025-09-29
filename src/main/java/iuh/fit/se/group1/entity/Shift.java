package iuh.fit.se.group1.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Shift {
    private Long shiftId;
    private String name;
    private LocalDate createdAt;
    public Shift() {
    }
    public Shift(Long shiftId, String name, LocalDate createdAt) {
        this.shiftId = shiftId;
        this.name = name;
        this.createdAt = createdAt;
    }
    public Long getShiftId() {
        return shiftId;
    }
    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Shift shift)) return false;
        return Objects.equals(shiftId, shift.shiftId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shiftId);
    }

    @Override
    public String toString() {
        return "Shift [shiftId=" + shiftId + ", name=" + name + ", createdAt=" + createdAt + "]";
    }
    

}

package iuh.fit.se.group1.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Shift {
    private Long shiftId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate createdAt;
    public Shift() {
    }

    public Shift(String name, Long shiftId, LocalDateTime startTime,  LocalDateTime endTime,LocalDate createdAt) {
        this.name = name;
        this.shiftId = shiftId;
        this.startTime = startTime;
        this.createdAt = createdAt;
        this.endTime = endTime;
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
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
        return "Shift{" +
                "shiftId=" + shiftId +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createdAt=" + createdAt +
                '}';
    }
}

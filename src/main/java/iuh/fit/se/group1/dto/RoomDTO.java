package iuh.fit.se.group1.dto;

import java.math.BigDecimal;

public class RoomDTO {
    private String roomType;
    private int capacity;
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    private BigDecimal overnightRate;
    private BigDecimal additionalHourRate;

    public RoomDTO() {
    }


    public RoomDTO(String roomType, int capacity, BigDecimal hourlyRate, BigDecimal dailyRate, BigDecimal overnightRate, BigDecimal additionalHourRate) {
        this.roomType = roomType;
        this.capacity = capacity;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
        this.overnightRate = overnightRate;
        this.additionalHourRate = additionalHourRate;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public BigDecimal getOvernightRate() {
        return overnightRate;
    }

    public void setOvernightRate(BigDecimal overnightRate) {
        this.overnightRate = overnightRate;
    }

    public BigDecimal getAdditionalHourRate() {
        return additionalHourRate;
    }

    public void setAdditionalHourRate(BigDecimal additionalHourRate) {
        this.additionalHourRate = additionalHourRate;
    }
}

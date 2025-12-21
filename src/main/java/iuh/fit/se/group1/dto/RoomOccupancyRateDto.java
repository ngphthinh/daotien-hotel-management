package iuh.fit.se.group1.dto;

/**
 * DTO cho tỉ lệ lấp đầy phòng
 */
public class RoomOccupancyRateDto {
    private int totalRooms;
    private int occupiedRooms;
    private double occupancyRate;

    public RoomOccupancyRateDto() {
    }

    public RoomOccupancyRateDto(int totalRooms, int occupiedRooms) {
        this.totalRooms = totalRooms;
        this.occupiedRooms = occupiedRooms;
        this.occupancyRate = totalRooms > 0 ? (occupiedRooms * 100.0 / totalRooms) : 0;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
        recalculateRate();
    }

    public int getOccupiedRooms() {
        return occupiedRooms;
    }

    public void setOccupiedRooms(int occupiedRooms) {
        this.occupiedRooms = occupiedRooms;
        recalculateRate();
    }

    public double getOccupancyRate() {
        return occupancyRate;
    }

    private void recalculateRate() {
        this.occupancyRate = totalRooms > 0 ? (occupiedRooms * 100.0 / totalRooms) : 0;
    }
}


package iuh.fit.se.group1.dto;

/**
 * DTO cho trạng thái phòng
 */
public class RoomStatusDto {
    private int occupiedRooms;
    private int availableRooms;
    private int checkedOutRooms;
    private int cancelledBookings;

    public RoomStatusDto() {
    }

    public RoomStatusDto(int occupiedRooms, int availableRooms, int checkedOutRooms, int cancelledBookings) {
        this.occupiedRooms = occupiedRooms;
        this.availableRooms = availableRooms;
        this.checkedOutRooms = checkedOutRooms;
        this.cancelledBookings = cancelledBookings;
    }

    public int getOccupiedRooms() {
        return occupiedRooms;
    }

    public void setOccupiedRooms(int occupiedRooms) {
        this.occupiedRooms = occupiedRooms;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }

    public int getCheckedOutRooms() {
        return checkedOutRooms;
    }

    public void setCheckedOutRooms(int checkedOutRooms) {
        this.checkedOutRooms = checkedOutRooms;
    }

    public int getCancelledBookings() {
        return cancelledBookings;
    }

    public void setCancelledBookings(int cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }
}


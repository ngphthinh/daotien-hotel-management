package iuh.fit.se.group1.dto;

public class RoomSelection {
    private Long id;
    private String number;
    private String roomType;

    public RoomSelection() {
    }

    public RoomSelection(Long id, String number, String roomType) {
        this.id = id;
        this.number = number;
        this.roomType = roomType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return number;
    }
}
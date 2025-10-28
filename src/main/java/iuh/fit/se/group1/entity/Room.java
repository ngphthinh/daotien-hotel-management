package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.RoomStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Room {

    private Long roomId;
    private String roomNumber;
    private RoomType roomType;
    private LocalDate createdAt;
    private RoomStatus roomStatus;
    private BigDecimal price;  // Thêm trường price dựa trên schema SQL (DECIMAL(18,2) -> BigDecimal cho độ chính xác)

    public Room(Long roomId, String roomNumber, RoomType roomType, LocalDate createdAt, RoomStatus roomStatus, BigDecimal price) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.createdAt = createdAt;
        this.roomStatus = roomStatus;
        this.price = price;
    }

    public Room() {
    }

    public Room(String roomNumber, RoomType roomType, RoomStatus roomStatus, BigDecimal price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.price = price;
    }

    public Room(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public LocalDate getCreateAt() {
        return createdAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createdAt = createAt;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.roomId ^ (this.roomId >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        return this.roomId == other.roomId;
    }

    @Override
    public String toString() {
        return "Room{" + "roomId=" + roomId + ", roomNumber=" + roomNumber + ", roomType=" + roomType + ", createAt=" + createdAt + ", roomStatus=" + roomStatus + ", price=" + price + '}';
    }

    public String getRoomTypeId() {
        if (roomType != null) {
            return roomType.getRoomTypeId();  // Giả sử RoomType có method getRoomTypeId()
        }
        return null;
    }

    public void setCreatedAt(LocalDate now) {
        this.createdAt = now;
    }
    
}
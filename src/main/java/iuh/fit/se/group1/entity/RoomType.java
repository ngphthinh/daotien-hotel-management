package iuh.fit.se.group1.entity;

import java.time.LocalDate;
import java.util.Objects;

public class RoomType {
    private String roomTypeId;
    private String name;
    private LocalDate createdAt;

    public RoomType(String name) {
        this.name = name;
    }

    public RoomType() {
    }

    public RoomType(String roomTypeId, String name, LocalDate createdAt) {
        this.roomTypeId = roomTypeId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
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
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.roomTypeId);
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
        final RoomType other = (RoomType) obj;
        return Objects.equals(this.roomTypeId, other.roomTypeId);
    }

    @Override
    public String toString() {
        return "RoomType{" + "roomTypeId=" + roomTypeId + ", name=" + name + ", createdAt=" + createdAt + '}';
    }
}

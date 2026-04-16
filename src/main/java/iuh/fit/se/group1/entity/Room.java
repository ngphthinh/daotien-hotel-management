package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Room {
    @Id
    private Long roomId;
    private String roomNumber;


    @ManyToOne
    @JoinColumn(name = "roomTypeId")
    private RoomType roomType;
    private LocalDate createdAt;
    @Enumerated
    private RoomStatus roomStatus;

    public Room(Long roomId, String roomNumber) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
    }


    public Room(String roomNumber, RoomType roomType, RoomStatus roomStatus) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
    }

    public Room(Long roomId) {
        this.roomId = roomId;
    }



    public String getRoomTypeId() {
        if (roomType != null) {
            return roomType.getRoomTypeId();  // Giả sử RoomType có method getRoomTypeId()
        }
        return null;
    }
}
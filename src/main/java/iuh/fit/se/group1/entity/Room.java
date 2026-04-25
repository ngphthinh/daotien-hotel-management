package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Builder
@SQLDelete(sql = "UPDATE Room SET isDeleted = 1 WHERE roomId = ?")
@SQLRestriction("isDeleted = 0")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long roomId;
    private String roomNumber;

    @ManyToOne
    @JoinColumn(name = "roomTypeId")
    private RoomType roomType;
    private LocalDate createdAt;
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;
    private boolean isDeleted;

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
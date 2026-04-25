package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho trạng thái phòng
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomStatusDto {
    private int occupiedRooms;
    private int availableRooms;
    private int checkedOutRooms;
    private int cancelledBookings;


}


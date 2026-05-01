package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.BookingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransferRoomRequest implements Serializable {
    private List<RoomViewDTO> selectedOldRooms;
    private List<RoomViewDTO> selectedNewRooms;
    private BookingType currentBookingType;
    private Long orderId;
}

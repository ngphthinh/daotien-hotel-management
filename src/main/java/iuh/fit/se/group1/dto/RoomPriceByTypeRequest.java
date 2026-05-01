package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.BookingType;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class RoomPriceByTypeRequest implements java.io.Serializable {
    private RoomViewDTO newRoom;
    private BookingType currentBookingType;
    private Long orderId;
    private RoomViewDTO referenceRoom;

}

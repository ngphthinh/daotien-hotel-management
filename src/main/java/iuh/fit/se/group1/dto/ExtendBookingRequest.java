package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.BookingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExtendBookingRequest implements java.io.Serializable {
    private Long orderId;
    private List<RoomViewDTO> roomsToExtend;
    private int extendValue;
    private BookingType bookingType;


}

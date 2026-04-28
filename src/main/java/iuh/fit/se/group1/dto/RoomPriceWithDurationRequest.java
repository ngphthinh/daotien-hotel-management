package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.BookingType;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomPriceWithDurationRequest implements Serializable {
    private RoomViewDTO room;
    private BookingType bookingType;
    private Long orderId;
}

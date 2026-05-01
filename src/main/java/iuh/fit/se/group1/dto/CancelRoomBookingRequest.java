package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.BookingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CancelRoomBookingRequest implements Serializable {
    private Long orderId;
    private Long roomId;
    private BookingType currentBookingType;
}

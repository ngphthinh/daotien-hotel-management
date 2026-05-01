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
public class CalculateExtensionAmountRequest implements Serializable {
    private List<RoomViewDTO> roomsToExtend;
    private BookingType bookingType;
    private int extendValue;
}

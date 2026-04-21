package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.BookingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingDTO {
    public long bookingId;
    public String bookingIdDisplay;
    public String guestName;
    public String cccd;
    public BookingType bookingType;
    public String rooms;
    public long orderId;
    public String orderTypeName;
}

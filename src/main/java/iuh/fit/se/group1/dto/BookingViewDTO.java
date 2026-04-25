package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.BookingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingViewDTO {
    private Long bookingId;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private BookingType bookingType;
    private RoomViewDTO room;
    private Long orderId;

    @Override
    public String toString() {
        return room.getRoomNumber();
    }

    public String displayName() {
        return "BookingViewDTO{" +
                "bookingId=" + bookingId +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", bookingType=" + bookingType +
                ", room=" + room +
                ", orderId=" + orderId +
                '}';
    }

}

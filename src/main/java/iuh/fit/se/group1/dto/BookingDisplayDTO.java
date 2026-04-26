package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingDisplayDTO implements Serializable {
    private Long bookingId;
    private String roomNumber;
    private String customerName;
    private String phoneNumber;
}

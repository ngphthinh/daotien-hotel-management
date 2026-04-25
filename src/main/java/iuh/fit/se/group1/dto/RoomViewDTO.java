package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomViewDTO
{
    private Long roomId;
    private String roomNumber;
    private RoomTypeDTO roomType;
    private RoomStatus roomStatus;

}

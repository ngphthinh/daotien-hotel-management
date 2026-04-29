package iuh.fit.se.group1.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckRoomCapacityResponse implements Serializable {
    private int usedSingleRooms;
    private int usedDoubleRooms;

    private int leftoverAdults;
    private int leftoverChildren;
    private int leftoverTotal;
}
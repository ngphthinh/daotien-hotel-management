package iuh.fit.se.group1.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRoomCountResponse implements Serializable {

    private RoomDTO singleRoom;
    private long singleAvailable;

    private RoomDTO doubleRoom;
    private long doubleAvailable;

    private long totalAvailable;
}
package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoomStatusBatchRequest implements Serializable {

    private List<Long> roomIds;

    private RoomStatus roomStatus;
}
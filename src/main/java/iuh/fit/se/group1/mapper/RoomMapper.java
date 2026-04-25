package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.RoomDTO;
import iuh.fit.se.group1.dto.RoomViewDTO;
import iuh.fit.se.group1.dto.RoomTypeDTO;
import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.util.Constants;

public class RoomMapper {
    private final RoomTypeMapper roomTypeMapper;

    public RoomMapper() {
        this.roomTypeMapper = new RoomTypeMapper();
    }

    public RoomViewDTO toRoomDTO(Room room) {
        if (room == null) return null;
        return RoomViewDTO.builder()
                .roomId(room.getRoomId())
                .roomNumber(room.getRoomNumber())
                .roomType(this.roomTypeMapper.toRoomTypeDTO(room.getRoomType()))
                .build();
    }


    public Room toRoom(RoomViewDTO roomDTO) {
        if (roomDTO == null) return null;
        return Room.builder()
                .roomId(roomDTO.getRoomId())
                .roomNumber(roomDTO.getRoomNumber())
                .roomType(this.roomTypeMapper.toRoomType(roomDTO.getRoomType()))
                .build();
    }


    public RoomDTO toRoomDTO(RoomType rt) {

        RoomTypeDTO roomTypeDTO = this.roomTypeMapper.toRoomTypeDTO(rt);

        if (roomTypeDTO == null) {
            return null;
        }
        int capacity = roomTypeDTO.getRoomTypeId().equals(Constants.SINGLE_ROOM_TYPE) ? 2 : 4;


        return new RoomDTO(
                roomTypeDTO.getRoomTypeId(),
                capacity,
                roomTypeDTO.getHourlyRate(),
                roomTypeDTO.getDailyRate(),
                roomTypeDTO.getOvernightRate(),
                roomTypeDTO.getAdditionalHourRate()
        );
    }

    public RoomDTO toRoomDTO(RoomTypeDTO rt) {


        if (rt == null) {
            return null;
        }
        int capacity = rt.getRoomTypeId().equals(Constants.SINGLE_ROOM_TYPE) ? 2 : 4;


        return new RoomDTO(
                rt.getRoomTypeId(),
                capacity,
                rt.getHourlyRate(),
                rt.getDailyRate(),
                rt.getOvernightRate(),
                rt.getAdditionalHourRate()
        );
    }
}

package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.RoomTypeDTO;
import iuh.fit.se.group1.entity.RoomType;

public class RoomTypeMapper {

    public RoomType toRoomType(RoomTypeDTO roomTypeDTO) {
        if (roomTypeDTO == null) {
            return null;
        }

        return RoomType.builder()
                .roomTypeId(roomTypeDTO.getRoomTypeId())
                .name(roomTypeDTO.getName())
                .hourlyRate(roomTypeDTO.getHourlyRate())
                .dailyRate(roomTypeDTO.getDailyRate())
                .overnightRate(roomTypeDTO.getOvernightRate())
                .additionalHourRate(roomTypeDTO.getAdditionalHourRate())
                .build();

    }

    public RoomTypeDTO toRoomTypeDTO(RoomType roomType) {
        if (roomType == null) {
            return null;
        }

        return RoomTypeDTO.builder()
                .roomTypeId(roomType.getRoomTypeId())
                .name(roomType.getName())
                .hourlyRate(roomType.getHourlyRate())
                .dailyRate(roomType.getDailyRate())
                .overnightRate(roomType.getOvernightRate())
                .additionalHourRate(roomType.getAdditionalHourRate())
                .build();
    }

}

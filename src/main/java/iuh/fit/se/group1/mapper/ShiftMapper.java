package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.ShiftDTO;
import iuh.fit.se.group1.entity.Shift;

public class ShiftMapper {

    public ShiftDTO toDTO(Shift shift) {

        if (shift == null)
            return null;
        return ShiftDTO.builder()
                .shiftId(shift.getShiftId())
                .name(shift.getName())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .build();

    }

    public Shift toEntity(ShiftDTO shift) {
        if (shift == null)
            return null;
        return Shift.builder()
                .shiftId(shift.getShiftId())
                .name(shift.getName())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .build();
    }
}

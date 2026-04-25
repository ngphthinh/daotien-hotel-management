package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.ShiftCloseDTO;
import iuh.fit.se.group1.entity.ShiftClose;

public class ShiftCloseMapper {

    private final EmployeeShiftMapper employeeShiftMapper;
    private final EmployeeMapper employeeMapper;

    public ShiftCloseMapper() {
        this.employeeShiftMapper = new EmployeeShiftMapper();
        this.employeeMapper = new EmployeeMapper();
    }


    public ShiftClose toShiftClose(ShiftCloseDTO shiftClose) {
        if (shiftClose == null) {
            return null;
        }
        return ShiftClose.builder()
                .shiftCloseId(shiftClose.getShiftCloseId())
                .employeeShift(employeeShiftMapper.toEmployeeShift(shiftClose.getEmployeeShift()))
                .totalRevenue(shiftClose.getTotalRevenue())
                .cashInDrawer(shiftClose.getCashInDrawer())
                .difference(shiftClose.getDifference())
                .note(shiftClose.getNote())
                .manager(employeeMapper.toEmployee(shiftClose.getManager()))
                .createdAt(shiftClose.getCreatedAt()).build();
    }

    public ShiftCloseDTO toShiftCloseDTO(ShiftClose shiftClose) {
        if (shiftClose == null) {
            return null;
        }

        return ShiftCloseDTO.builder()
                .shiftCloseId(shiftClose.getShiftCloseId())
                .employeeShift(employeeShiftMapper.toEmployeeShiftDTO(shiftClose.getEmployeeShift()))
                .totalRevenue(shiftClose.getTotalRevenue())
                .cashInDrawer(shiftClose.getCashInDrawer())
                .difference(shiftClose.getDifference())
                .note(shiftClose.getNote())
                .manager(employeeMapper.toDTO(shiftClose.getManager()))
                .createdAt(shiftClose.getCreatedAt()).build();
    }
}

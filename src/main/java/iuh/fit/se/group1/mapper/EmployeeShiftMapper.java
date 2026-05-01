package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.dto.EmployeeShiftDTO;
import iuh.fit.se.group1.dto.ShiftDTO;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;

public class EmployeeShiftMapper {

    private final EmployeeMapper employeeMapper;
    private final ShiftMapper shiftMapper;

    public EmployeeShiftMapper() {
        this.employeeMapper = new EmployeeMapper();
        this.shiftMapper = new ShiftMapper();
    }

    public EmployeeShiftDTO toEmployeeShiftDTO(EmployeeShift employeeShift) {

        if (employeeShift == null) {
            return null;
        }

        return EmployeeShiftDTO.builder()
                .employeeShiftId(employeeShift.getEmployeeShiftId())
                .employee(employeeMapper.toDTO(employeeShift.getEmployee()))
                .shift(shiftMapper.toDTO(employeeShift.getShift()))
                .systemAmount(employeeShift.getSystemAmount())
                .actualAmount(employeeShift.getActualAmount())
                .difference(employeeShift.getDifference())
                .shiftDate(employeeShift.getShiftDate())
                .createdAt(employeeShift.getCreatedAt())
                .build();
    }

    public EmployeeShift toEmployeeShift(EmployeeShiftDTO employeeShift) {
        if (employeeShift == null) {
            return null;
        }
        return EmployeeShift.builder()
                .employeeShiftId(employeeShift.getEmployeeShiftId())
                .employee(employeeMapper.toEmployee(employeeShift.getEmployee()))
                .shift(shiftMapper.toEntity(employeeShift.getShift()))
                .systemAmount(employeeShift.getSystemAmount())
                .actualAmount(employeeShift.getActualAmount())
                .difference(employeeShift.getDifference())
                .shiftDate(employeeShift.getShiftDate())
                .createdAt(employeeShift.getCreatedAt())
                .build();
    }

}

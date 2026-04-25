package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class ShiftCloseDTO {
    private Long shiftCloseId;
    private EmployeeShiftDTO employeeShift;
    private BigDecimal totalRevenue;
    private BigDecimal cashInDrawer;
    private BigDecimal difference;
    private String note;
    private EmployeeDTO manager;
    private LocalDateTime createdAt;
}

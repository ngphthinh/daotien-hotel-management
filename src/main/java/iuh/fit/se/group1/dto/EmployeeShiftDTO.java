package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.entity.DenominationDetail;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.Shift;
import iuh.fit.se.group1.entity.ShiftClose;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmployeeShiftDTO {
    private Long employeeShiftId;
    private EmployeeDTO employee;
    private ShiftDTO shift;
    private BigDecimal systemAmount;
    private BigDecimal actualAmount;
    private BigDecimal difference;
    private LocalDate shiftDate;
    private LocalDate createdAt;


}

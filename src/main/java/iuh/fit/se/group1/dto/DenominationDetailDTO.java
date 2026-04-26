package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.enums.DenominationLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DenominationDetailDTO implements Serializable {
    private Long denominationDetailId;
    private DenominationLabel denomination;
    private int quantity;
    private EmployeeShiftDTO employeeShift;
    private LocalDate createdAt;
}

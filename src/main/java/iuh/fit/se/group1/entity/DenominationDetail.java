package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.DenominationLabel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString
@EqualsAndHashCode(of = "denominationDetailId")
@Entity

public class DenominationDetail {
    @Id
    private	Long denominationDetailId;
    private DenominationLabel denomination;
    private	int quantity;
    @ManyToOne
    @JoinColumn(name = "employeeShiftId")
    private EmployeeShift employeeShift;
    private LocalDate createdAt;


}

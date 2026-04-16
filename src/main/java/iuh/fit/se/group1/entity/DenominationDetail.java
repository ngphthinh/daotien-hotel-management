package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.DenominationLabel;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private	Long denominationDetailId;
    private DenominationLabel denomination;
    private	int quantity;
    @ManyToOne
    @JoinColumn(name = "employeeShiftId")
    private EmployeeShift employeeShift;
    private LocalDate createdAt;


}

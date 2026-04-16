package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.DenominationLabel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

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


    public DenominationDetail(DenominationLabel denomination, int quantity) {
        this.denomination = denomination;
        this.quantity = quantity;
        this.createdAt = LocalDate.now();
    }
}

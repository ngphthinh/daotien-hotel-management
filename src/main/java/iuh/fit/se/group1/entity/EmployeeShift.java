package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString
@EqualsAndHashCode(of = "employeeShiftId")
@Entity
public class EmployeeShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private	Long employeeShiftId;
    @ManyToOne
    @JoinColumn(name = "employeeId")
    private	Employee employee;

    @ManyToOne
    @JoinColumn(name = "shiftId")
    private	Shift shift;
    @OneToMany(mappedBy = "shiftCloseId")
    private Set<ShiftClose> shiftClose;
    private BigDecimal systemAmount;
    private	BigDecimal actualAmount;
    private	BigDecimal difference;
    private LocalDate shiftDate;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "employeeShift")
    private Set<DenominationDetail> denominationDetails;

    public EmployeeShift(Employee employee, Shift shift) {
        this.employee = employee;
        this.shift = shift;
    }
}

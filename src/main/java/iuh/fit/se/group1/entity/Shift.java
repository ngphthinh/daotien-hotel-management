package iuh.fit.se.group1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Shift {
    @Id
    private Long shiftId;
    private String name;
    private String startTime;
    private String endTime;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "shift")
    private Set<EmployeeShift> employeeShifts;
}

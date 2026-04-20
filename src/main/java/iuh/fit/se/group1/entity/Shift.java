package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"employeeShifts"})
@Entity
@Builder
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long shiftId;
    @Column(columnDefinition = "varchar(50)")
    private String name;
    @Column(columnDefinition = "varchar(10)")
    private String startTime;
    @Column(columnDefinition = "varchar(10)")
    private String endTime;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "shift")
    private Set<EmployeeShift> employeeShifts;
}

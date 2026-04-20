package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "employeeId")
@Entity
public class Employee {
    @Id
    private Long employeeId;
    private String fullName;
    @Column(columnDefinition = "varchar(10)")
    private String phone;
    @Column(columnDefinition = "varchar(255)")
    private String email;
    private boolean gender;
    @Column(columnDefinition = "varchar(12)")
    private String citizenId;
    private LocalDate hireDate;
    @OneToOne
    @JoinColumn(name = "accountId")
    private Account account;
    private byte[] avt;
    private LocalDate createdAt;
    @OneToMany(mappedBy = "employee")
    private Set<Order> order;

    @OneToMany(mappedBy = "employeePayment")
    private Set<Order> orderPayment;

    @OneToMany(mappedBy = "employee")
    private Set<EmployeeShift> employeeShifts;
    public Employee(Long employeeId) {
        this.employeeId = employeeId;
    }

}
package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"account", "orders", "orderPayment", "employeeShifts"})
@EqualsAndHashCode(of = "employeeId")
@Entity
@SQLDelete(sql = "UPDATE Employee SET isDeleted = true WHERE employeeId = ?")
@SQLRestriction("isDeleted = false")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long employeeId;
    @Column(columnDefinition = "nvarchar(255)")
    private String fullName;
    private String phone;
    private String email;
    private boolean gender;
    private String citizenId;
    private LocalDate hireDate;
    @OneToOne
    @JoinColumn(name = "accountId")
    private Account account;
    @Lob
    private byte[] avt;
    private LocalDate createdAt;
    @OneToMany(mappedBy = "employee")
    private Set<Order> orders;

    @OneToMany(mappedBy = "employeePayment")
    private Set<Order> orderPayment;

    @OneToMany(mappedBy = "employee")
    private Set<EmployeeShift> employeeShifts;

    private boolean isDeleted;

    public Employee(Long employeeId) {
        this.employeeId = employeeId;
    }

}
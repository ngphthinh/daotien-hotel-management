package iuh.fit.se.group1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private String phone;
    private String email;
    private boolean gender;
    private String citizenId;
    private LocalDate hireDate;
    private Account account;
    private byte[] avt;
    private LocalDate createdAt;
    @OneToMany(mappedBy = "employee")
    private Set<Order> order;


}
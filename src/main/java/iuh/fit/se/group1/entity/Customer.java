package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"orders"})
@EqualsAndHashCode(of = "customerId")
@Entity
@Builder
@SQLDelete(sql = "UPDATE Customer SET isDeleted = 1 WHERE customerId = ?")
@SQLRestriction("isDeleted = 0")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(columnDefinition = "nvarchar(255)")
    private String fullName;
    @Column(columnDefinition = "varchar(10)")
    private String phone;
    @Column(columnDefinition = "varchar(255)")
    private String email;
    @Column(columnDefinition = "varchar(12)")
    private String citizenId;
    private boolean gender;
    private LocalDate dateOfBirth;
    private LocalDate createdAt;
    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;

    private boolean isDeleted;
}

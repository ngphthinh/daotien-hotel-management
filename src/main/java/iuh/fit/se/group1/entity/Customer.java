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
@SQLDelete(sql = "UPDATE Customer SET isDeleted = true WHERE customerId = ?")
@SQLRestriction("isDeleted = false")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String fullName;
    private String phone;
    private String email;
    private String citizenId;
    private boolean gender;
    private LocalDate dateOfBirth;
    private LocalDate createdAt;
    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;

    private boolean isDeleted;
}

package iuh.fit.se.group1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"orders"})
@EqualsAndHashCode(of = "customerId")
@Entity
public class Customer {
    @Id
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
}

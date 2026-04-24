package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@SQLDelete(sql = "UPDATE Account SET isDeleted = 1 WHERE accountId = ?")
@SQLRestriction("isDeleted = 0")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;

    @Column(columnDefinition = "varchar(50)")
    private String username;
    @Column(columnDefinition = "varchar(255)")
    private String password;
    private LocalDate createdAt;

    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private Role role;
}

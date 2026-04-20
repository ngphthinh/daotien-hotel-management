package iuh.fit.se.group1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"accounts"})
@Entity
public class Role {
    @Id
    @Column(columnDefinition = "varchar(30)")
    private String roleId;
    private String roleName;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "role")
    private Set<Account> accounts;

    public Role(String roleId) {
        this.roleId = roleId;
    }

}

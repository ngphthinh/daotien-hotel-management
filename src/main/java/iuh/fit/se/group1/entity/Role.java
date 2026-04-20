package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"accounts"})
@Entity
@Builder
//@Table(name = "Roles")
public class Role {
    @Id
    @Column(columnDefinition = "varchar(30)")
    private String roleId;
    @Column(columnDefinition = "nvarchar(100)")
    private String roleName;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "role")
    private Set<Account> accounts;

    public Role(String roleId) {
        this.roleId = roleId;
    }

}

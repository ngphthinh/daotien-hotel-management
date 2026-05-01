package iuh.fit.se.group1.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoleDTO  implements Serializable {
    private String roleId;
    private String roleName;
}

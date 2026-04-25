package iuh.fit.se.group1.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoleDTO {
    private String roleId;
    private String roleName;
}

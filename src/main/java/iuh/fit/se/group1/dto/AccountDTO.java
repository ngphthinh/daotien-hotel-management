package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountDTO {
    private String accountId;
    private String username;
    private String password;
    private RoleDTO role;
}

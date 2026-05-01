package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.RoleDTO;
import iuh.fit.se.group1.entity.Role;

public class RoleMapper {
    public RoleDTO toDTO(Role role) {
        if (role == null)
            return null;
        return RoleDTO.builder()
                .roleId(role.getRoleId())
                .roleName(role.getRoleName())
                .build();
    }

    public Role toRole(RoleDTO roleDTO) {
        if (roleDTO == null)
            return null;
        return Role.builder()
                .roleId(roleDTO.getRoleId())
                .roleName(roleDTO.getRoleName())
                .build();
    }
}

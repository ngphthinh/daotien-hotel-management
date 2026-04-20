package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.repository.jpa.RoleRepositoryImpl;

public class RoleService {
    private final RoleRepositoryImpl roleRepositoryImpl;

    public RoleService() {
        this.roleRepositoryImpl = new RoleRepositoryImpl();
    }

    public Role createRole(Role role) {
        return roleRepositoryImpl.save(role);
    }

    public void deleteRole(String roleId) {
        roleRepositoryImpl.deleteById(roleId);
    }

    public java.util.List<Role> getAllRoles() {
        return roleRepositoryImpl.findAll();
    }

    public Role getRoleById(String roleId) {
        return roleRepositoryImpl.findById(roleId);
    }

    public boolean roleExists(String roleId) {
        return roleRepositoryImpl.existsById(roleId);
    }
}

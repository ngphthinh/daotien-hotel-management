package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.repository.RoleRepository;

public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService() {
        this.roleRepository = new RoleRepository();
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
    }

    public java.util.List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(String roleId) {
        return roleRepository.findById(roleId);
    }

    public boolean roleExists(String roleId) {
        return roleRepository.existsById(roleId);
    }
}

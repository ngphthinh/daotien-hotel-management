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

}

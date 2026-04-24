package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.repository.jpa.RoleRepositoryImpl;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RoleService extends Service {
    private final RoleRepositoryImpl roleRepositoryImpl;

    public RoleService() {
        this.roleRepositoryImpl = new RoleRepositoryImpl();
    }

    public Role createRole(Role role) {
//        return roleRepositoryImpl.save(role);
        return doInTransaction(entityManager -> roleRepositoryImpl.save(entityManager, role));
    }

    public void deleteRole(String roleId) {
//        roleRepositoryImpl.deleteById(roleId);
        doInTransactionVoid(entityManager -> roleRepositoryImpl.deleteById(entityManager, roleId));
    }

    public java.util.List<Role> getAllRoles() {
//        return roleRepositoryImpl.findAll();
        return doInTransaction(roleRepositoryImpl::findAll);
    }

    public Role getRoleById(EntityManager em, String roleId) {
//        return roleRepositoryImpl.findById(roleId);
        return roleRepositoryImpl.findById(em, roleId);
    }

    public Role getRoleById(String roleId) {
//        return roleRepositoryImpl.findById(roleId);
        return doInTransaction(em -> roleRepositoryImpl.findById(em, roleId));
    }

    public boolean roleExists(String roleId) {
//        return roleRepositoryImpl.existsById(roleId);
        return doInTransaction(entityManager -> roleRepositoryImpl.existsById(entityManager, roleId));
    }


}

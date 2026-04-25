package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.RoleDTO;
import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.mapper.RoleMapper;
import iuh.fit.se.group1.repository.jpa.RoleRepositoryImpl;
import jakarta.persistence.EntityManager;

public class RoleService extends Service {
    private final RoleRepositoryImpl roleRepositoryImpl;
    private final RoleMapper roleMapper;

    public RoleService() {
        this.roleRepositoryImpl = new RoleRepositoryImpl();
        this.roleMapper = new RoleMapper();
    }

    public RoleDTO createRole(RoleDTO role) {

        Role roleEntity = roleMapper.toRole(role);

        return doInTransaction(entityManager -> roleMapper.toDTO(roleRepositoryImpl.save(entityManager, roleEntity)));
    }

    public void deleteRole(String roleId) {
//        roleRepositoryImpl.deleteById(roleId);
        doInTransactionVoid(entityManager -> roleRepositoryImpl.deleteById(entityManager, roleId));
    }

    public java.util.List<RoleDTO> getAllRoles() {
//        return roleRepositoryImpl.findAll();
        return doInTransaction(roleRepositoryImpl::findAll).stream().map(roleMapper::toDTO).toList();
    }

    public Role getRoleEntityById(EntityManager em, String roleId) {
        return roleRepositoryImpl.findById(em, roleId);
    }
    public RoleDTO getRoleById(EntityManager em, String roleId) {
        return roleMapper.toDTO(roleRepositoryImpl.findById(em, roleId));
    }

    public RoleDTO getRoleById(String roleId) {
//        return roleRepositoryImpl.findById(roleId);
        return doInTransaction(em -> roleMapper.toDTO(getRoleEntityById(em, roleId)));
    }

    public boolean roleExists(String roleId) {
//        return roleRepositoryImpl.existsById(roleId);
        return doInTransaction(entityManager -> roleRepositoryImpl.existsById(entityManager, roleId));
    }


}

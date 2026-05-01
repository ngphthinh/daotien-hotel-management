package iuh.fit.se.group1.repository.interfaces;

import jakarta.persistence.EntityManager;

public interface RoleRepository {
    boolean existsById(EntityManager em, String roleId);
}

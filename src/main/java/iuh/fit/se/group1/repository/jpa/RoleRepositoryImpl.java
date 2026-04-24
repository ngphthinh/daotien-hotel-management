package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Role;
import jakarta.persistence.EntityManager;

public class RoleRepositoryImpl extends AbstractRepositoryImpl<Role, String> implements iuh.fit.se.group1.repository.interfaces.RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }

    @Override
    public boolean existsById(EntityManager em, String roleId) {
        String query = """
                SELECT COUNT(r) FROM Role r
                WHERE r.roleId = :roleId
                """;

        return
                em.createQuery(query, Long.class)
                        .setParameter("roleId", roleId)
                        .getSingleResult() > 0;
    }
}

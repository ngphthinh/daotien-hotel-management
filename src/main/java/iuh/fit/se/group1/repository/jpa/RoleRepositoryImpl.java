package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.repository.AbstractRepositoryImpl;

public class RoleRepositoryImpl extends AbstractRepositoryImpl<Role, String> implements iuh.fit.se.group1.repository.interfaces.RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }

    @Override
    public boolean existsById(String roleId) {
        String query = """
                SELECT COUNT(r) FROM Role r
                WHERE r.roleId = :roleId
                """;

        return callInTransaction(em ->
                em.createQuery(query, Long.class)
                        .setParameter("roleId", roleId)
                        .getSingleResult() > 0
        );
    }
}

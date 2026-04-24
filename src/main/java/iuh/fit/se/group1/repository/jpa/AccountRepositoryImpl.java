package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.infrastructure.JPAUtil;
import jakarta.persistence.EntityManager;

public class AccountRepositoryImpl extends AbstractRepositoryImpl<Account, String> implements iuh.fit.se.group1.repository.interfaces.AccountRepository {
    public AccountRepositoryImpl() {
        super(Account.class);
    }

    @Override
    public Account findByUsername(EntityManager em, String username) {
        String query = """
                SELECT a FROM Account a
                WHERE a.username = :username AND a.isDeleted = false
                """;

        return em.createQuery(query, Account.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

    }

    @Override
    public boolean updatePassword(EntityManager em, String accountId, String newHashedPassword) {
        String query = """
                UPDATE Account a
                SET a.password = :newPass
                WHERE a.accountId = :accountId AND a.isDeleted = false
                """;

        return em.createQuery(query)
                .setParameter("accountId", accountId)
                .setParameter("newPass", newHashedPassword)
                .executeUpdate() > 0;
    }


}

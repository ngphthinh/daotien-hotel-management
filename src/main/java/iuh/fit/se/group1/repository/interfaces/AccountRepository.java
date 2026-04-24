package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Account;
import jakarta.persistence.EntityManager;

public interface AccountRepository {
    Account findByUsername(EntityManager em,String username);

    boolean updatePassword(EntityManager em, String accountId, String newHashedPassword);
}

package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Account;

public interface AccountRepository {
    Account findByUsername(String username);

    boolean updatePassword(String accountId, String newHashedPassword);
}

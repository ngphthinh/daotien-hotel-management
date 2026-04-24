package iuh.fit.se.group1.service;

import jakarta.persistence.EntityManager;
import org.mindrot.jbcrypt.BCrypt;
import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.repository.jpa.AccountRepositoryImpl;
import iuh.fit.se.group1.util.PasswordUtil;

import java.time.LocalDate;

public class AccountService extends Service {
    private final AccountRepositoryImpl accountRepositoryImpl;

    public AccountService() {
        this.accountRepositoryImpl = new AccountRepositoryImpl();
    }

    public Account createAccount(EntityManager em, Account account) {
        account.setPassword(PasswordUtil.hashPassword(account.getPassword()));
        account.setCreatedAt(LocalDate.now());
        return accountRepositoryImpl.save(em, account);
    }

    public Account updateAccount(EntityManager em, Account account) {
        return accountRepositoryImpl.update(em, account);

    }

    public boolean changePassword(String username, String oldPass, String newPass) {
//        Account acc = doInTransaction(em -> accountRepositoryImpl.findByUsername(em, username));
//        if (acc == null) return false;
//
//        if (!BCrypt.checkpw(oldPass, acc.getPassword())) {
//            return false;
//        }
//
//        String newHashed = BCrypt.hashpw(newPass, BCrypt.gensalt());
//        return doInTransaction(entityManager -> accountRepositoryImpl.updatePassword(entityManager, acc.getAccountId(), newHashed));
        return doInTransaction(entityManager -> {
            Account acc = accountRepositoryImpl.findByUsername(entityManager, username);
            if (acc == null) return false;

            if (!BCrypt.checkpw(oldPass, acc.getPassword())) {
                return false;
            }

            String newHashed = BCrypt.hashpw(newPass, BCrypt.gensalt());
            return accountRepositoryImpl.updatePassword(entityManager, acc.getAccountId(), newHashed);
        });
    }


}

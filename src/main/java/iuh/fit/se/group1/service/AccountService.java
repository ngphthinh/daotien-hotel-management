package iuh.fit.se.group1.service;

import org.mindrot.jbcrypt.BCrypt;
import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.repository.jpa.AccountRepositoryImpl;
import iuh.fit.se.group1.util.PasswordUtil;

import java.time.LocalDate;

public class AccountService {
    private final AccountRepositoryImpl accountRepositoryImpl;

    public AccountService() {
        this.accountRepositoryImpl = new AccountRepositoryImpl();
    }

    public Account createAccount(Account account) {
        account.setPassword(PasswordUtil.hashPassword(account.getPassword()));
        account.setCreatedAt(LocalDate.now());
        return accountRepositoryImpl.save(account);
    }

    public Account updateAccount(Account account) {
        return accountRepositoryImpl.update(account);
    }

    public boolean changePassword(String username, String oldPass, String newPass) {
        Account acc = accountRepositoryImpl.findByUsername(username);
        if (acc == null) return false;

        if (!BCrypt.checkpw(oldPass, acc.getPassword())) {
            return false;
        }

        String newHashed = BCrypt.hashpw(newPass, BCrypt.gensalt());
        return accountRepositoryImpl.updatePassword(acc.getAccountId(), newHashed);
    }

    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    private static AccountService instance;

}

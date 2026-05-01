package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.AccountDTO;
import iuh.fit.se.group1.mapper.AccountMapper;
import jakarta.persistence.EntityManager;
import org.mindrot.jbcrypt.BCrypt;
import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.repository.jpa.AccountRepositoryImpl;
import iuh.fit.se.group1.util.PasswordUtil;

import java.time.LocalDate;

public class AccountService extends Service {
    private final AccountRepositoryImpl accountRepositoryImpl;

    private final AccountMapper accountMapper;

    public AccountService() {
        this.accountRepositoryImpl = new AccountRepositoryImpl();
        this.accountMapper = new AccountMapper();
    }

    public Account createAccount(EntityManager em, Account account) {
        account.setPassword(PasswordUtil.hashPassword(account.getPassword()));
        account.setCreatedAt(LocalDate.now());
        return accountRepositoryImpl.save(em, account);
    }

    public AccountDTO updateAccount(EntityManager em, Account account) {
        return accountMapper.toDTO(accountRepositoryImpl.update(em, account));

    }

    public boolean changePassword(String username, String oldPass, String newPass) {
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

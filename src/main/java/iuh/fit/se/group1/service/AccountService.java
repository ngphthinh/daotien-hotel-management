package iuh.fit.se.group1.service;
import org.mindrot.jbcrypt.BCrypt;
import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.repository.AccountRepository;
import iuh.fit.se.group1.util.PasswordUtil;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService() {
        this.accountRepository = new AccountRepository();
    }

    public Account createAccount(Account account) {
        account.setPassword(PasswordUtil.hashPassword(account.getPassword()));
        return accountRepository.save(account);
    }

    public Account updateAccount(Account account) {
        return accountRepository.update(account);
    }

    public boolean changePassword(String username, String oldPass, String newPass) {
    Account acc = accountRepository.findByUsername(username);
    if (acc == null) return false;

    if (!BCrypt.checkpw(oldPass, acc.getPassword())) {
        return false;
    }

    String newHashed = BCrypt.hashpw(newPass, BCrypt.gensalt());
    return accountRepository.updatePassword(acc.getAccountId(), newHashed);
}

}

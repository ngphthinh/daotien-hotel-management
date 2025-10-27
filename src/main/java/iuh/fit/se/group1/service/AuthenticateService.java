package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.repository.AccountRepository;
import iuh.fit.se.group1.util.PasswordUtil;
import iuh.fit.se.group1.util.PropertiesReader;

public class AuthenticateService {
    private final AccountRepository accountRepository;

    public AuthenticateService() {
        this.accountRepository = new AccountRepository();
    }

    public Account authenticate (String username, String password) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return null;
        }
        if (!PasswordUtil.checkPassword(password, account.getPassword())) {
            return null;
        }
        return account;
    }

    public void resetPassword (String username){
        Account account = accountRepository.findByUsername(username);
        String resetPassword = PropertiesReader.getInstance().get("daotien.password");
        if (account != null) {
            String hashedPassword = PasswordUtil.hashPassword(resetPassword);
            account.setPassword(hashedPassword);
            accountRepository.update(account);
        }
    }
}



package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.repository.AccountRepository;
import iuh.fit.se.group1.repository.EmployeeRepository;
import iuh.fit.se.group1.util.PasswordUtil;
import iuh.fit.se.group1.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticateService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticateService.class);
    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;

    public AuthenticateService() {
        this.accountRepository = new AccountRepository();
        this.employeeRepository = new EmployeeRepository();
    }

    public Account authenticate (String username, String password) {
        log.info("🔍 Attempting login - Username: '{}'", username);

        Account account = accountRepository.findByUsername(username);

        if (account == null) {
            log.error("❌ Account NOT FOUND: '{}'", username);
            return null;
        }

        log.info("✅ Account FOUND: '{}'", username);
        log.info("📝 Hash from DB: {}", account.getPassword());
        log.info("🔑 Plain password: {}", password);

        boolean isPasswordCorrect = PasswordUtil.checkPassword(password, account.getPassword());

        log.info("🔐 Password check result: {}", isPasswordCorrect);

        if (!isPasswordCorrect) {
            log.error("❌ WRONG PASSWORD for: '{}'", username);
            return null;
        }

        log.info("✅ LOGIN SUCCESS: '{}'", username);
        return account;
    }

    public Employee getEmployeeByAccountId(Long accountId) {
        if (accountId == null) {
            return null;
        }

        return employeeRepository.findByAccountId(accountId);
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



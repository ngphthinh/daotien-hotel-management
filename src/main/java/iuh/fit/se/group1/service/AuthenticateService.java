package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.repository.jpa.AccountRepositoryImpl;
import iuh.fit.se.group1.repository.jpa.EmployeeRepositoryImpl;
import iuh.fit.se.group1.util.PasswordUtil;
import iuh.fit.se.group1.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticateService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticateService.class);
    private final AccountRepositoryImpl accountRepositoryImpl;
    private final EmployeeRepositoryImpl employeeRepositoryImpl;

    public AuthenticateService() {
        this.accountRepositoryImpl = new AccountRepositoryImpl();
        this.employeeRepositoryImpl = new EmployeeRepositoryImpl();
    }

    public Account authenticate(String username, String password) {
        AppLogger.info("Attempting login - Username: '{}'", username);

        Account account = accountRepositoryImpl.findByUsername(username);

        if (account == null) {
            log.error("Account NOT FOUND: '{}'", username);
            return null;
        }

        boolean isPasswordCorrect = PasswordUtil.checkPassword(password, account.getPassword());

        if (!isPasswordCorrect) {
            AppLogger.error(String.format("WRONG PASSWORD for: '{%s}'", username));
            return null;
        }

        AppLogger.info("LOGIN SUCCESS: '{}'", username);
        return account;
    }

    public Employee getEmployeeByAccountId(String accountId) {
        if (accountId == null) {
            return null;
        }

        return employeeRepositoryImpl.findByAccountId(accountId);
    }

    public void resetPassword(String username) {
        Account account = accountRepositoryImpl.findByUsername(username);
        String resetPassword = PropertiesReader.getInstance().get("daotien.password");
        if (account != null) {
            String hashedPassword = PasswordUtil.hashPassword(resetPassword);
            account.setPassword(hashedPassword);
            accountRepositoryImpl.update(account);
        }
    }
}



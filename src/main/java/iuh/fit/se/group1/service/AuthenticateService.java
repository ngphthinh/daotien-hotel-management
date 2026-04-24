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

public class AuthenticateService extends Service {
    private static final Logger log = LoggerFactory.getLogger(AuthenticateService.class);
    private final AccountRepositoryImpl accountRepositoryImpl;
    private final EmployeeRepositoryImpl employeeRepositoryImpl;

    private static final String DEFAULT_PASSWORD_PROPERTY = "daotien.password";

    public AuthenticateService() {
        this.accountRepositoryImpl = new AccountRepositoryImpl();
        this.employeeRepositoryImpl = new EmployeeRepositoryImpl();
    }

    public Account authenticate(String username, String password) {
        AppLogger.info("Attempting login - Username: '{}'", username);

//        Account account = accountRepositoryImpl.findByUsername(username);

        Account account = doInTransaction(em -> accountRepositoryImpl.findByUsername(em, username));

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

//        return employeeRepositoryImpl.findByAccountId(accountId);
        return doInTransaction(em -> employeeRepositoryImpl.findByAccountId(em, accountId));
    }

    public void resetPassword(String username) {

        doInTransactionVoid(em -> {

            Account account = accountRepositoryImpl.findByUsername(em, username);
            String resetPassword = PropertiesReader.getInstance().get(DEFAULT_PASSWORD_PROPERTY);
            if (account != null) {
                String hashedPassword = PasswordUtil.hashPassword(resetPassword);
                account.setPassword(hashedPassword);
                accountRepositoryImpl.update(em, account);
                AppLogger.info("Password reset successful for: '{}'", username);
            }
        });

    }
}



package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.dto.AccountDTO;
import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.mapper.AccountMapper;
import iuh.fit.se.group1.mapper.EmployeeMapper;
import iuh.fit.se.group1.repository.jpa.AccountRepositoryImpl;
import iuh.fit.se.group1.repository.jpa.EmployeeRepositoryImpl;
import iuh.fit.se.group1.util.PasswordUtil;
import iuh.fit.se.group1.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticateService extends Service {
    private static final Logger log = LoggerFactory.getLogger(AuthenticateService.class);
    private final AccountRepositoryImpl accountRepositoryImpl;
    private final AccountMapper accountMapper;
    private static final String DEFAULT_PASSWORD_PROPERTY = "daotien.password";
    private final EmployeeRepositoryImpl employeeRepositoryImpl;
    private final EmployeeMapper employeeMapper;

    public AuthenticateService() {
        this.employeeRepositoryImpl = new EmployeeRepositoryImpl();
        this.accountRepositoryImpl = new AccountRepositoryImpl();
        this.accountMapper = new AccountMapper();
        this.employeeMapper = new EmployeeMapper();
    }

    public AccountDTO authenticate(String username, String password) {
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
//        return account;
        return accountMapper.toDTO(account);
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

    public EmployeeDTO validateManager(String username, String password) {
        return doInTransaction(em -> employeeMapper.toDTO(employeeRepositoryImpl.validateManager(em, username, password)));
    }
}



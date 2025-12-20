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
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            return null;
        }
        if (!PasswordUtil.checkPassword(password, account.getPassword())) {
            return null;
        }
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



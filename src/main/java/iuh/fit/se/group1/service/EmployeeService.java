package iuh.fit.se.group1.service;

import java.text.Normalizer;
import java.util.List;

import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.repository.jpa.EmployeeRepositoryImpl;
import iuh.fit.se.group1.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepositoryImpl employeeRepositoryImpl;
    private final AccountService accountService;
    private final RoleService roleService;

    public EmployeeService() {
        this.accountService = new AccountService();
        this.roleService = new RoleService();
        this.employeeRepositoryImpl = new EmployeeRepositoryImpl();
    }

    public int count(){
        return employeeRepositoryImpl.count();
    }


    public Employee createEmployee(Employee employee, String roleId) {
        Role role = roleService.getRoleById(roleId);
        if (role == null) {
            log.error("Role with ID {} not found. Cannot create employee.", roleId);
            throw new IllegalArgumentException("Invalid role ID: " + roleId);
        }
        Account account = new Account();
        account.setUsername("username");
        account.setPassword(PropertiesReader.getInstance().get("daotien.password"));
        account.setRole(role);
        Account accountSave = accountService.createAccount(account);
        // Tao nhan vien
        employee.setAccount(accountSave);
        Employee employeeSave = employeeRepositoryImpl.save(employee);
        // Tạo account khi thêm nhân viên
        String username = generateUsername(employeeSave);

        // update employee với account vừa tạo
        accountSave.setUsername(username);
        accountSave = accountService.updateAccount(accountSave);

        employeeSave.setAccount(accountSave);
        return employeeRepositoryImpl.update(employeeSave);
    }

    public Employee getEmployeeByCitizenId(String citizenId) {
        return employeeRepositoryImpl.findByCitizenId(citizenId);
    }

    private String generateUsername(Employee entitySave) {
        String fullName = entitySave.getFullName();

        String normalized = Normalizer.normalize(fullName, Normalizer.Form.NFD);
        String noAccent = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String[] nameParts = noAccent.trim().split("\\s+");
        String lastName = nameParts[nameParts.length - 1];

        return String.format("%s%d", lastName.toLowerCase(), entitySave.getEmployeeId());
    }

    public void deleteEmployee(Long employeeId) {
        employeeRepositoryImpl.deleteById(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepositoryImpl.findAll();
    }


    public Employee updateEmployee(Employee employee) {

        return employeeRepositoryImpl.update(employee);
    }

    public List<Employee> getEmployeeByKeyword(String keyword){
        return employeeRepositoryImpl.findByIdOrNameOrPhoneNumber(keyword);
    }
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepositoryImpl.findById(employeeId);
    }
    public Employee existsByCitizenId(String citizenId){
        return employeeRepositoryImpl.findByCitizenId(citizenId);
    }
}

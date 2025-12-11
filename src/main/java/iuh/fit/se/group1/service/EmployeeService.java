package iuh.fit.se.group1.service;

import java.text.Normalizer;
import java.util.List;

import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.repository.EmployeeRepository;
import iuh.fit.se.group1.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final AccountService accountService;
    private final RoleService roleService;

    public EmployeeService() {
        this.accountService = new AccountService();
        this.roleService = new RoleService();
        this.employeeRepository = new EmployeeRepository();
    }

    public int count(){
        return employeeRepository.count();
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
        Employee employeeSave = employeeRepository.save(employee);
        // Tạo account khi thêm nhân viên
        String username = generateUsername(employeeSave);

        // update employee với account vừa tạo
        accountSave.setUsername(username);
        accountSave = accountService.updateAccount(accountSave);

        employeeSave.setAccount(accountSave);
        return employeeRepository.update(employeeSave);
    }

    public Employee getEmployeeByCitizenId(String citizenId) {
        return employeeRepository.findByCitizenId(citizenId);
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
        employeeRepository.deleteById(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


    public Employee updateEmployee(Employee employee) {

        return employeeRepository.update(employee);
    }

    public List<Employee> getEmployeeByKeyword(String keyword){
        return employeeRepository.findByIdOrNameOrPhoneNumber(keyword);
    }
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }
    public Employee existsByCitizenId(String citizenId){
        return employeeRepository.findByCitizenId(citizenId);
    }
}

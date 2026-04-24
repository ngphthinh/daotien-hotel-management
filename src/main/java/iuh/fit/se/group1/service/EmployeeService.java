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

public class EmployeeService extends Service {
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepositoryImpl employeeRepositoryImpl;
    private final AccountService accountService;
    private final RoleService roleService;

    private final BookingService bookingService;

    private final RoomToolsService roomToolsService;

    public EmployeeService() {
        this.bookingService = new BookingService();
        this.accountService = new AccountService();
        this.roleService = new RoleService();
        this.employeeRepositoryImpl = new EmployeeRepositoryImpl();
        this.roomToolsService = new RoomToolsService();
    }

    public int count() {
//        return employeeRepositoryImpl.count();
        return doInTransaction(employeeRepositoryImpl::count);
    }


    public Employee createEmployee(Employee employee, String roleId) {

        return doInTransaction(entityManager -> {
            Role role = roleService.getRoleById(entityManager, roleId);
            if (role == null) {
                throw new IllegalArgumentException("Invalid role ID: " + roleId);
            }

            Account account = new Account();
            account.setUsername("username");
            account.setPassword(PropertiesReader.getInstance().get("daotien.password"));
            account.setRole(role);

            Account accountSave = accountService.createAccount(entityManager, account);

            // set account trước khi save employee
            employee.setAccount(accountSave);
            Employee employeeSave = employeeRepositoryImpl.save(entityManager, employee);

            // generate username
            String username = generateUsername(employeeSave);
            accountSave.setUsername(username);

            accountService.updateAccount(entityManager, accountSave);

            return employeeSave;
        });
    }

    public Employee getEmployeeByCitizenId(String citizenId) {
//        return employeeRepositoryImpl.findByCitizenId(citizenId);
        return doInTransaction(entityManager -> employeeRepositoryImpl.findByCitizenId(entityManager, citizenId));
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
        // kiểm tra xem employee đang có hóa đơn có tồn tại bookng không
        doInTransactionVoid(entityManager -> {
            Employee employee = employeeRepositoryImpl.findById(entityManager, employeeId);
            if (employee == null) {
                throw new IllegalArgumentException("Employee not found with ID: " + employeeId);
            }

            var orders = roomToolsService.getAllOrders();
            if (orders.stream().anyMatch(order -> order.getEmployee().getEmployeeId().equals(employeeId))) {
                throw new IllegalStateException("Cannot delete employee with active orders");
            }


            employeeRepositoryImpl.deleteById(entityManager, employeeId);
        });
//        employeeRepositoryImpl.deleteById(employeeId);
//        doInTransactionVoid(entityManager -> employeeRepositoryImpl.deleteById(entityManager, employeeId));
    }

    public List<Employee> getAllEmployees() {
//        return employeeRepositoryImpl.findAll();
        return doInTransaction(employeeRepositoryImpl::findAll);
    }


    public Employee updateEmployee(Employee employee) {

//        return employeeRepositoryImpl.update(employee);
        return doInTransaction(entityManager -> employeeRepositoryImpl.update(entityManager, employee));
    }

    public List<Employee> getEmployeeByKeyword(String keyword) {
//        return employeeRepositoryImpl.findByIdOrNameOrPhoneNumber(keyword);
        return doInTransaction(entityManager -> employeeRepositoryImpl.findByIdOrNameOrPhoneNumber(entityManager, keyword));
    }

    public Employee getEmployeeById(Long employeeId) {
//        return employeeRepositoryImpl.findById(employeeId);
        return doInTransaction(entityManager -> employeeRepositoryImpl.findById(entityManager, employeeId));
    }

    public Employee existsByCitizenId(String citizenId) {
//        return employeeRepositoryImpl.findByCitizenId(citizenId);
        return doInTransaction(entityManager -> employeeRepositoryImpl.findByCitizenId(entityManager, citizenId));
    }

    public List<Employee> findAllByRoleId(String string) {
        return doInTransaction(entityManager -> employeeRepositoryImpl.findAllByRoleId(entityManager, string));
    }
}

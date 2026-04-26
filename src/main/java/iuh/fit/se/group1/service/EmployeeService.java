package iuh.fit.se.group1.service;

import java.text.Normalizer;
import java.util.List;

import iuh.fit.se.group1.dto.AccountDTO;
import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.mapper.EmployeeMapper;
import iuh.fit.se.group1.repository.jpa.EmployeeRepositoryImpl;
import iuh.fit.se.group1.repository.jpa.OrderRepositoryImpl;
import iuh.fit.se.group1.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeService extends Service {
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepositoryImpl employeeRepositoryImpl;
    private final AccountService accountService;
    private final RoleService roleService;

    private final EmployeeMapper employeeMapper;

    private final OrderRepositoryImpl orderRepository;


    public EmployeeService() {
        this.employeeMapper = new EmployeeMapper();
        this.accountService = new AccountService();
        this.roleService = new RoleService();
        this.employeeRepositoryImpl = new EmployeeRepositoryImpl();
        this.orderRepository = new OrderRepositoryImpl();
    }

    public int count() {
//        return employeeRepositoryImpl.count();
        return doInTransaction(employeeRepositoryImpl::count);
    }

    public EmployeeDTO getEmployeeByAccountId(String accountId) {
        if (accountId == null) {
            return null;
        }
        return doInTransaction(em -> employeeMapper.toDTO(employeeRepositoryImpl.findByAccountId(em, accountId)));
    }


    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO, String roleId) {

        return doInTransaction(entityManager -> {

            Employee employee = employeeMapper.toEmployee(employeeDTO);

            Role role = roleService.getRoleEntityById(entityManager, roleId);
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


            System.out.println("Sau khi luu employee" + employeeMapper.toDTO(employee));
            System.out.println("Sau khi luu account" + accountSave);
            // generate username
            String username = generateUsername(employeeSave);
            accountSave.setUsername(username);

            System.out.println("Username sau khi generate: " + accountSave.getUsername());

            accountService.updateAccount(entityManager, accountSave);

            System.out.println("Account sau khi update: " + accountSave);

            return employeeMapper.toDTO(employeeSave);
        });
    }

    public EmployeeDTO getEmployeeByCitizenId(String citizenId) {
//        return employeeRepositoryImpl.findByCitizenId(citizenId);
        return doInTransaction(entityManager -> employeeMapper.toDTO(employeeRepositoryImpl.findByCitizenId(entityManager, citizenId)));
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

            boolean exists = orderRepository.existsByEmployeeIdAndCompleteYet(entityManager, employeeId);
            if (exists) {
                throw new IllegalStateException("Cannot delete employee with active orders");
            }


            employeeRepositoryImpl.deleteById(entityManager, employeeId);
        });
    }
//        employeeRepositoryImpl.deleteById(employeeId);
//        doInTransactionVoid(entityManager -> employeeRepositoryImpl.deleteById(entityManager, employeeId));

    public List<EmployeeDTO> getAllEmployees() {
//        return employeeRepositoryImpl.findAll();
        return doInTransaction(employeeRepositoryImpl::findAll).stream().map(employeeMapper::toDTO).toList();
    }


    public EmployeeDTO updateEmployee(EmployeeDTO employee) {

//        return employeeRepositoryImpl.update(employee);
        return doInTransaction(entityManager -> employeeMapper.toDTO(employeeRepositoryImpl.update(entityManager, employeeMapper.toEmployee(employee))));
    }

    public List<EmployeeDTO> getEmployeeByKeyword(String keyword) {
//        return employeeRepositoryImpl.findByIdOrNameOrPhoneNumber(keyword);
        return doInTransaction(entityManager -> employeeRepositoryImpl.findByIdOrNameOrPhoneNumber(entityManager, keyword)).stream().map(employeeMapper::toDTO).toList();
    }

    public EmployeeDTO getEmployeeById(Long employeeId) {
//        return employeeRepositoryImpl.findById(employeeId);
        return doInTransaction(entityManager -> employeeMapper.toDTO(employeeRepositoryImpl.findById(entityManager, employeeId)));
    }

    public EmployeeDTO existsByCitizenId(String citizenId) {
//        return employeeRepositoryImpl.findByCitizenId(citizenId);
        return doInTransaction(entityManager -> employeeMapper.toDTO(employeeRepositoryImpl.findByCitizenId(entityManager, citizenId)));
    }

    public List<EmployeeDTO> findAllByRoleId(String string) {
        return doInTransaction(entityManager -> employeeRepositoryImpl.findAllByRoleId(entityManager, string)).stream().map(employeeMapper::toDTO).toList();
    }


}

package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends Repository<Employee, Long> {

    List<Employee> findAllByRoleId(String roleId);

    Employee findByCitizenId(String citizenId);

    List<Employee> findByIdOrNameOrPhoneNumber(String keyword);

    int count();

    Employee findByAccountId(String accountId);
}

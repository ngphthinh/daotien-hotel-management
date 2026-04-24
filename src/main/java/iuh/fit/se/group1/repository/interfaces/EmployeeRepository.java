package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Employee;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface EmployeeRepository extends Repository<Employee, Long> {

    List<Employee> findAllByRoleId(EntityManager em, String roleId);

    Employee findByCitizenId(EntityManager em, String citizenId);

    List<Employee> findByIdOrNameOrPhoneNumber(EntityManager em, String keyword);

    int count(EntityManager em);

    Employee findByAccountId(EntityManager em, String accountId);
}

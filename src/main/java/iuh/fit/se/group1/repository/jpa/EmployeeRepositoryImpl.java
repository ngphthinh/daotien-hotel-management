package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.repository.interfaces.EmployeeRepository;

import java.util.List;

public class EmployeeRepositoryImpl extends AbstractRepositoryImpl<Employee, Long> implements EmployeeRepository {
    public EmployeeRepositoryImpl() {
        super(Employee.class);
    }

    @Override
    public Employee update(Employee entity) {
        return callInTransaction(em -> {

            Employee managed = em.find(Employee.class, entity.getEmployeeId());
            if (managed == null) {
                throw new RuntimeException("Employee not found");
            }

            managed.setFullName(entity.getFullName());
            managed.setPhone(entity.getPhone());
            managed.setEmail(entity.getEmail());
            managed.setHireDate(entity.getHireDate());
            managed.setCitizenId(entity.getCitizenId());
            managed.setGender(entity.isGender());
            managed.setAvt(entity.getAvt());

            managed.setAccount(entity.getAccount());

            return managed;
        });
    }

    @Override
    public List<Employee> findAllByRoleId(String roleId) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT DISTINCT e
                        FROM Employee e
                        JOIN FETCH e.account a
                        JOIN FETCH a.role r
                        WHERE r.roleId = :roleId AND e.isDeleted = false
                        ORDER BY e.employeeId
                    """;

            return em.createQuery(jpql, Employee.class)
                    .setParameter("roleId", roleId)
                    .getResultList();
        });
    }

    @Override
    public Employee findByCitizenId(String citizenId) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT e
                        FROM Employee e
                        JOIN FETCH e.account a
                        JOIN FETCH a.role
                        WHERE e.citizenId = :citizenId AND e.isDeleted = false
                    """;

            return em.createQuery(jpql, Employee.class)
                    .setParameter("citizenId", citizenId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        });
    }

    @Override
    public List<Employee> findByIdOrNameOrPhoneNumber(String keyword) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT DISTINCT e
                        FROM Employee e
                        JOIN FETCH e.account a
                        JOIN FETCH a.role
                        WHERE CAST(e.employeeId AS string) LIKE :kw
                           OR LOWER(e.fullName) LIKE LOWER(:kw)
                           OR e.phone LIKE :kw AND e.isDeleted = false
                        ORDER BY e.employeeId, e.fullName
                    """;

            return em.createQuery(jpql, Employee.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        });
    }

    @Override
    public int count() {
        return callInTransaction(em ->
                Math.toIntExact(
                        em.createQuery("SELECT COUNT(e) FROM Employee e WHERE e.isDeleted = false ", Long.class)
                                .getSingleResult()
                )
        );
    }

    @Override
    public Employee findByAccountId(String accountId) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT e
                        FROM Employee e
                        JOIN FETCH e.account a
                        JOIN FETCH a.role
                        WHERE a.accountId = :accountId AND e.isDeleted = false AND a.isDeleted = false
                    """;

            return em.createQuery(jpql, Employee.class)
                    .setParameter("accountId", accountId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        });
    }
}

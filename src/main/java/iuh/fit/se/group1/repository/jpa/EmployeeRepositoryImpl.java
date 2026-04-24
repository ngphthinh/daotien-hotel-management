package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.repository.interfaces.EmployeeRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EmployeeRepositoryImpl extends AbstractRepositoryImpl<Employee, Long> implements EmployeeRepository {
    public EmployeeRepositoryImpl() {
        super(Employee.class);
    }

    @Override
    public Employee update(EntityManager em, Employee entity) {


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

    }

    @Override
    public List<Employee> findAllByRoleId(EntityManager em, String roleId) {

        String jpql = """
                    SELECT DISTINCT e
                    FROM Employee e
                    JOIN FETCH e.account a
                    JOIN FETCH a.role r
                    WHERE r.roleId = :roleId AND e.isDeleted = false
                    ORDER BY e.employeeId ASC, e.fullName ASC
                """;

        return em.createQuery(jpql, Employee.class)
                .setParameter("roleId", roleId)
                .getResultList();
    }

    @Override
    public Employee findByCitizenId(EntityManager em, String citizenId) {

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
    }

    @Override
    public List<Employee> findByIdOrNameOrPhoneNumber(EntityManager em, String keyword) {

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
    }

    @Override
    public int count(EntityManager em) {
        return
                Math.toIntExact(
                        em.createQuery("SELECT COUNT(e) FROM Employee e WHERE e.isDeleted = false ", Long.class)
                                .getSingleResult()
                );
    }

    @Override
    public Employee findByAccountId(EntityManager em, String accountId) {

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
    }

    @Override
    public List<Employee> findAll(EntityManager em) {
        String query = """
                    SELECT e
                    FROM Employee e
                    JOIN FETCH e.account a
                    JOIN FETCH a.role
                    WHERE e.isDeleted = false AND a.isDeleted = false
                    ORDER BY e.employeeId ASC, e.fullName ASC
                """;
        return em.createQuery(query, Employee.class).getResultList();
    }
}

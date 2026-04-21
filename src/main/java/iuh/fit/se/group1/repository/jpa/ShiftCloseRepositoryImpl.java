package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.entity.ShiftClose;
import iuh.fit.se.group1.repository.interfaces.ShiftCloseRepository;
import iuh.fit.se.group1.util.PasswordUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ShiftCloseRepositoryImpl extends AbstractRepositoryImpl<ShiftClose, Long> implements ShiftCloseRepository {

    public ShiftCloseRepositoryImpl() {
        super(ShiftClose.class);
    }

    @Override
    public ShiftClose save(ShiftClose entity) {
        return callInTransaction(em -> {

            if (entity.getTotalRevenue() != null && entity.getCashInDrawer() != null) {
                BigDecimal moneyOpenShift = new BigDecimal("5000000");
                BigDecimal difference = entity.getCashInDrawer()
                        .subtract(entity.getTotalRevenue().add(moneyOpenShift));
                entity.setDifference(difference);
            } else {
                entity.setDifference(BigDecimal.ZERO);
            }

            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDateTime.now());
            }

            em.persist(entity);
            return entity;
        });
    }

    @Override
    public List<ShiftClose> findByEmployeeShift(EmployeeShift employeeShift) {
        return callInTransaction(em ->
                em.createQuery("""
                                    FROM ShiftClose sc
                                    WHERE sc.employeeShift.employeeShiftId = :id
                                """, ShiftClose.class)
                        .setParameter("id", employeeShift.getEmployeeShiftId())
                        .getResultList()
        );
    }

    @Override
    public BigDecimal getTotalCashRevenueForShift(Long employeeShiftId) {
        return callInTransaction(em -> {
            Object result = em.createNativeQuery("""
                                SELECT COALESCE(SUM(O.totalAmount), 0)
                                FROM Orders O
                                INNER JOIN EmployeeShift ES ON O.employeePaymentId = ES.employeeId
                                INNER JOIN Shift S ON ES.shiftId = S.shiftId
                                WHERE ES.employeeShiftId = ?
                                  AND CAST(O.orderDate AS DATE) = ES.shiftDate
                                  AND O.orderTypeId = 1
                                  AND O.paymentType = 'CASH'
                                  AND (
                                        (S.startTime < S.endTime 
                                            AND CAST(O.orderDate AS TIME) >= CAST(S.startTime AS TIME)
                                            AND CAST(O.orderDate AS TIME) <= CAST(S.endTime AS TIME))
                                     OR
                                        (S.startTime >= S.endTime
                                            AND CAST(O.orderDate AS TIME) >= CAST(S.startTime AS TIME))
                                  )
                            """)
                    .setParameter(1, employeeShiftId)
                    .getSingleResult();

            return result != null ? (BigDecimal) result : BigDecimal.ZERO;
        });
    }

    @Override
    public Employee validateManager(String username, String password) {
        return callInTransaction(em -> {
            List<Object[]> rs = em.createNativeQuery("""
                                SELECT e.employeeId, e.fullName, e.phone, e.email, a.password
                                FROM Employee e
                                JOIN Account a ON e.accountId = a.accountId
                                WHERE a.username = ? AND a.roleId = 'MANAGER'
                            """)
                    .setParameter(1, username)
                    .getResultList();

            if (rs.isEmpty()) return null;

            Object[] row = rs.get(0);
            String hashed = (String) row[4];
            if (!PasswordUtil.checkPassword(password, hashed)) return null;


            Employee e = new Employee();
            e.setEmployeeId(((Number) row[0]).longValue());
            e.setFullName((String) row[1]);
            e.setPhone((String) row[2]);
            e.setEmail((String) row[3]);

            return e;
        });
    }

    @Override
    public Employee getManagerById(Long managerId) {
        return callInTransaction(em -> {
            List<Object[]> rs = em.createNativeQuery("""
                                SELECT e.employeeId, e.fullName, e.phone, e.email
                                FROM Employee e
                                JOIN Account a ON e.accountId = a.accountId
                                WHERE e.employeeId = ? AND a.roleId = 'MANAGER'
                            """)
                    .setParameter(1, managerId)
                    .getResultList();

            if (rs.isEmpty()) return null;

            Object[] row = rs.get(0);

            Employee e = new Employee();
            e.setEmployeeId(((Number) row[0]).longValue());
            e.setFullName((String) row[1]);
            e.setPhone((String) row[2]);
            e.setEmail((String) row[3]);

            return e;
        });
    }

    @Override
    public String getManagerNameById(Long managerId) {
        return callInTransaction(em ->
                (String) em.createQuery("""
                                    SELECT e.fullName
                                    FROM Employee e
                                    WHERE e.employeeId = :id
                                """)
                        .setParameter("id", managerId)
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
        );
    }
}

package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.repository.interfaces.EmployeeShiftRepository;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class EmployeeShiftRepositoryImpl extends AbstractRepositoryImpl<EmployeeShift, Long> implements EmployeeShiftRepository {

    public EmployeeShiftRepositoryImpl() {
        super(EmployeeShift.class);
    }

    @Override
    public List<EmployeeShift> findAll(EntityManager em) {
        return
                em.createQuery("""
                                    SELECT es
                                    FROM EmployeeShift es
                                    JOIN FETCH es.employee
                                    JOIN FETCH es.shift
                                """, EmployeeShift.class)
                        .getResultList();
    }

    @Override
    public List<EmployeeShift> findByShiftDate(EntityManager em, LocalDate date) {
        return
                em.createQuery("""
                                    SELECT es
                                    FROM EmployeeShift es
                                    JOIN FETCH es.employee e
                                    JOIN FETCH es.shift s
                                    WHERE es.shiftDate = :date
                                """, EmployeeShift.class)
                        .setParameter("date", date)
                        .getResultList();
    }

    @Override
    public EmployeeShift findByIdWithDetails(EntityManager em, Long employeeShiftId) {
        return
                em.createQuery("""
                                    SELECT es
                                    FROM EmployeeShift es
                                    JOIN FETCH es.employee e
                                    JOIN FETCH es.shift s
                                    WHERE es.employeeShiftId = :id
                                """, EmployeeShift.class)
                        .setParameter("id", employeeShiftId)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
    }

    @Override
    public BigDecimal getTotalCashRevenueForShift(EntityManager em, Long employeeShiftId) {
        Object result = em.createNativeQuery("""
                                SELECT COALESCE(SUM(O.totalAmount), 0)
                                FROM Orders O
                                INNER JOIN Booking B ON O.orderId = B.orderId
                                WHERE B.employeeShiftId = ?
                                  AND O.orderTypeId = 1
                                  AND O.paymentType = 'CASH'
                        """)
                .setParameter(1, employeeShiftId)
                .getSingleResult();

        return result != null ? (BigDecimal) result : BigDecimal.ZERO;
    }
}

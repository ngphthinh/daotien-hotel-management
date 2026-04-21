package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.enums.PaymentType;
import iuh.fit.se.group1.repository.interfaces.EmployeeShiftRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class EmployeeShiftRepositoryImpl extends AbstractRepositoryImpl<EmployeeShift, Long> implements EmployeeShiftRepository {

    public EmployeeShiftRepositoryImpl() {
        super(EmployeeShift.class);
    }

    @Override
    public List<EmployeeShift> findAll() {
        return callInTransaction(em ->
                em.createQuery("""
                                    SELECT es
                                    FROM EmployeeShift es
                                    JOIN FETCH es.employee
                                    JOIN FETCH es.shift
                                """, EmployeeShift.class)
                        .getResultList()
        );
    }

    @Override
    public List<EmployeeShift> findByShiftDate(LocalDate date) {
        return callInTransaction(em ->
                em.createQuery("""
                                    SELECT es
                                    FROM EmployeeShift es
                                    JOIN FETCH es.employee e
                                    JOIN FETCH es.shift s
                                    WHERE es.shiftDate = :date
                                """, EmployeeShift.class)
                        .setParameter("date", date)
                        .getResultList()
        );
    }

    @Override
    public EmployeeShift findByIdWithDetails(Long employeeShiftId) {
        return callInTransaction(em ->
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
                        .orElse(null)
        );
    }

    @Override
    public BigDecimal getTotalCashRevenueForShift(Long employeeShiftId) {
        return callInTransaction(em -> {
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
        });
    }
}

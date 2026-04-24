package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.EmployeeShift;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeShiftRepository {
    List<EmployeeShift> findByShiftDate(EntityManager em, LocalDate date);

    EmployeeShift findByIdWithDetails(EntityManager em, Long employeeShiftId);

    BigDecimal getTotalCashRevenueForShift(EntityManager em, Long employeeShiftId);
}

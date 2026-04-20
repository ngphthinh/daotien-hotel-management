package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.EmployeeShift;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeShiftRepository {
    List<EmployeeShift> findByShiftDate(LocalDate date);

    EmployeeShift findByIdWithDetails(Long employeeShiftId);

    BigDecimal getTotalCashRevenueForShift(Long employeeShiftId);
}

package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.entity.ShiftClose;

import java.math.BigDecimal;
import java.util.List;

public interface ShiftCloseRepository {
    List<ShiftClose> findByEmployeeShift(EmployeeShift employeeShift);

    BigDecimal getTotalCashRevenueForShift(Long employeeShiftId);

    Employee validateManager(String username, String password);

    Employee getManagerById(Long managerId);

    String getManagerNameById(Long managerId);
}

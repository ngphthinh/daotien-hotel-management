package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.entity.ShiftClose;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;

public interface ShiftCloseRepository {
    List<ShiftClose> findByEmployeeShift(EntityManager em,EmployeeShift employeeShift);

    BigDecimal getTotalCashRevenueForShift(EntityManager em,Long employeeShiftId);

    Employee validateManager(EntityManager em, String username, String password);

    Employee getManagerById(Long managerId);

    String getManagerNameById(Long managerId);

    List<Object[]> getRecentShiftNotes(EntityManager em);
}

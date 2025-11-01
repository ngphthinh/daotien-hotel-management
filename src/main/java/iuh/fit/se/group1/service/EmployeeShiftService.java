/**
 * @ (#) EmployeeShiftService.java   1.0     30/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.service;


import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.repository.EmployeeShiftRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet 
 * @version: 1.0
 * @created: 30/10/2025
 */

public class EmployeeShiftService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeShiftService.class);
    private final EmployeeShiftRepository employeeShiftRepository;

    public EmployeeShiftService() {
        this.employeeShiftRepository = new EmployeeShiftRepository();
    }

    public EmployeeShift addEmployeeShift(EmployeeShift employeeShift) {
        log.info("Adding shift {} for employee {}",
                employeeShift.getShift().getShiftId(),
                employeeShift.getEmployee().getEmployeeId());
        return employeeShiftRepository.save(employeeShift);
    }

    public EmployeeShift updateEmployeeShift(EmployeeShift employeeShift) {
        log.info("Updating EmployeeShift {}", employeeShift.getEmployeeShiftId());
        return employeeShiftRepository.update(employeeShift);
    }

    public void deleteEmployeeShift(Long employeeShiftId) {
        log.info("Deleting EmployeeShift {}", employeeShiftId);
        employeeShiftRepository.deleteById(employeeShiftId);
    }

    public EmployeeShift findEmployeeShiftById(Long employeeShiftId) {
        log.info("Finding EmployeeShift by ID {}", employeeShiftId);
        return employeeShiftRepository.findById(employeeShiftId);
    }

    public List<EmployeeShift> getAllEmployeeShifts() {
        log.info("Fetching all EmployeeShifts");
        return employeeShiftRepository.findAll();
    }

    public List<EmployeeShift> getShiftsByEmployeeAndDate(Long employeeId, LocalDate date) {
        log.info("Fetching shifts for employee {} on date {}", employeeId, date);
        List<EmployeeShift> allShifts = employeeShiftRepository.findByShiftDate(date);
        return allShifts.stream()
                .filter(es -> es.getEmployee().getEmployeeId().equals(employeeId))
                .collect(Collectors.toList());
    }
    public List<EmployeeShift> getAllShiftsByDate(LocalDate date) {
        log.info("Fetching all shifts on date {}", date);
        return employeeShiftRepository.findByShiftDate(date);
    }
    public EmployeeShift getEmployeeShiftWithDetails(Long employeeShiftId) {
        EmployeeShiftRepository repository = new EmployeeShiftRepository();
        return repository.findByIdWithDetails(employeeShiftId);
    }

    /**
     * Lấy tổng doanh thu của ca làm việc
     */
    public BigDecimal getTotalRevenueForShift(Long employeeShiftId) {
        EmployeeShiftRepository repository = new EmployeeShiftRepository();
        return repository.getTotalRevenueForShift(employeeShiftId);
    }
    public boolean isShiftActive(EmployeeShift shift) {
        try {
            if (shift == null || shift.getShift() == null) {
                log.warn("Shift or shift info is null");
                return false;
            }

            String startStr = shift.getShift().getStartTime();
            String endStr = shift.getShift().getEndTime();

            if (startStr == null || startStr.isEmpty() || endStr == null || endStr.isEmpty()) {
                log.warn("Shift start or end time is null/empty for shift id {}", shift.getEmployeeShiftId());
                return false;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime now = LocalTime.now();
            LocalTime startTime = LocalTime.parse(startStr, formatter);
            LocalTime endTime = LocalTime.parse(endStr, formatter);

            // Ca qua đêm (vd: 22:00 - 06:00)
            if (endTime.isBefore(startTime)) {
                return !now.isBefore(startTime) || !now.isAfter(endTime);
            } else {
                // Ca bình thường
                return !now.isBefore(startTime) && !now.isAfter(endTime);
            }

        } catch (Exception e) {
            log.error("Error checking shift active status for shift id {}", shift.getEmployeeShiftId(), e);
            return false;
        }
    }


    /**
     * Lấy ca làm việc đang mở (chưa đóng) và đang trong thời gian làm việc
     */
    public EmployeeShift getActiveOpenShift(Long employeeId, LocalDate date) {
        log.info("Finding active open shift for employee {} on date {}", employeeId, date);

        ShiftCloseService shiftCloseService = new ShiftCloseService();
        List<EmployeeShift> todayShifts = getShiftsByEmployeeAndDate(employeeId, date);

        return todayShifts.stream()
                .filter(shift -> shiftCloseService.getShiftCloseByEmployeeShift(shift).isEmpty()) // Chưa đóng
                .filter(this::isShiftActive) // Đang trong giờ làm việc
                .findFirst()
                .orElse(null);
    }
}

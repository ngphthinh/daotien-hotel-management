/**
 * @ (#) ShiftService.java   1.0     30/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Shift;
import iuh.fit.se.group1.repository.ShiftRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 * @description
 * @author: Nguyen Tran Quoc Viet 
 * @version: 1.0
 * @created: 30/10/2025
 */

public class ShiftService {
    private static final Logger log = LoggerFactory.getLogger(ShiftService.class);

    private final ShiftRepository shiftRepository;

    public ShiftService() {
        this.shiftRepository = new ShiftRepository();
    }

    // Create a new Shift
    public Shift createShift(Shift shift) {
        log.info("Creating new Shift: {}", shift.getName());
        Shift savedShift = shiftRepository.save(shift);
        log.info("Shift created with ID: {}", savedShift.getShiftId());
        return savedShift;
    }

    // Find a Shift by ID
    public Shift getShiftById(Long id) {
        log.info("Fetching Shift by ID: {}", id);
        Shift shift = shiftRepository.findById(id);
        if (shift != null) {
            log.info("Shift found: {}", shift.getName());
        } else {
            log.warn("Shift with ID {} not found", id);
        }
        return shift;
    }

    // Update an existing Shift
    public Shift updateShift(Shift shift) {
        log.info("Updating Shift ID {}: {}", shift.getShiftId(), shift.getName());
        Shift updatedShift = shiftRepository.update(shift);
        log.info("Shift updated successfully: ID {}", updatedShift.getShiftId());
        return updatedShift;
    }

    // Delete a Shift by ID
    public void deleteShift(Long id) {
        log.info("Deleting Shift with ID: {}", id);
        shiftRepository.deleteById(id);
        log.info("Shift deleted successfully: ID {}", id);
    }

    // Get all Shifts
    public List<Shift> getAllShifts() {
        log.info("Fetching all Shifts");
        List<Shift> shifts = shiftRepository.findAll();
        log.info("Total Shifts found: {}", shifts.size());
        return shifts;
    }
}

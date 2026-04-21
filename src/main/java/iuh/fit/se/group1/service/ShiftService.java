/**
 * @ (#) ShiftService.java   1.0     30/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Shift;
import iuh.fit.se.group1.repository.jpa.ShiftRepositoryImpl;
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

    private final ShiftRepositoryImpl shiftRepositoryImpl;

    public ShiftService() {
        this.shiftRepositoryImpl = new ShiftRepositoryImpl();
    }

    // Create a new Shift
    public Shift createShift(Shift shift) {
        log.info("Creating new Shift: {}", shift.getName());
        Shift savedShift = shiftRepositoryImpl.save(shift);
        log.info("Shift created with ID: {}", savedShift.getShiftId());
        return savedShift;
    }

    // Find a Shift by ID
    public Shift getShiftById(Long id) {
        log.info("Fetching Shift by ID: {}", id);
        Shift shift = shiftRepositoryImpl.findById(id);
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
        Shift updatedShift = shiftRepositoryImpl.update(shift);
        log.info("Shift updated successfully: ID {}", updatedShift.getShiftId());
        return updatedShift;
    }

    // Delete a Shift by ID
    public void deleteShift(Long id) {
        log.info("Deleting Shift with ID: {}", id);
        shiftRepositoryImpl.deleteById(id);
        log.info("Shift deleted successfully: ID {}", id);
    }

    // Get all Shifts
    public List<Shift> getAllShifts() {
        return shiftRepositoryImpl.findAll();
    }
}

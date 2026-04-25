/**
 * @ (#) ShiftService.java   1.0     30/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.ShiftDTO;
import iuh.fit.se.group1.entity.Shift;
import iuh.fit.se.group1.mapper.ShiftMapper;
import iuh.fit.se.group1.repository.jpa.ShiftRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet
 * @version: 1.0
 * @created: 30/10/2025
 */

public class ShiftService extends Service {
    private static final Logger log = LoggerFactory.getLogger(ShiftService.class);

    private final ShiftRepositoryImpl shiftRepositoryImpl;
    private final ShiftMapper shiftMapper = new ShiftMapper();

    public ShiftService() {
        this.shiftRepositoryImpl = new ShiftRepositoryImpl();
    }

    public ShiftDTO createShift(ShiftDTO shift) {
        Shift savedShift = doInTransaction(entityManager -> shiftRepositoryImpl.save(entityManager, shiftMapper.toEntity(shift)));
        return shiftMapper.toDTO(savedShift);
    }

    public ShiftDTO getShiftById(Long id) {
        Shift shift = doInTransaction(entityManager -> shiftRepositoryImpl.findById(entityManager, id));
        return shiftMapper.toDTO(shift);
    }

    // Update an existing Shift
    public ShiftDTO updateShift(ShiftDTO shift) {
        Shift updatedShift = doInTransaction(entityManager -> shiftRepositoryImpl.update(entityManager, shiftMapper.toEntity(shift)));
        return shiftMapper.toDTO(updatedShift);
    }

    // Delete a Shift by ID
    public void deleteShift(Long id) {
        doInTransactionVoid(entityManager -> shiftRepositoryImpl.deleteById(entityManager, id));
    }

    public List<ShiftDTO> getAllShifts() {
        List<Shift> shifts = doInTransaction(shiftRepositoryImpl::findAll);
        return shifts.stream().map(shiftMapper::toDTO).collect(Collectors.toList());
    }
}

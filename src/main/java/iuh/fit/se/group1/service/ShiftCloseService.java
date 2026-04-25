/**
 * @ (#) ShiftCloseService.java   1.0     31/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.service;


import iuh.fit.se.group1.dto.EmployeeShiftDTO;
import iuh.fit.se.group1.dto.ShiftCloseDTO;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.entity.ShiftClose;
import iuh.fit.se.group1.mapper.ShiftCloseMapper;
import iuh.fit.se.group1.repository.jpa.ShiftCloseRepositoryImpl;
import iuh.fit.se.group1.repository.interfaces.ShiftCloseRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet
 * @version: 1.0
 * @created: 31/10/2025
 */

public class ShiftCloseService extends Service {

    private final ShiftCloseRepositoryImpl repository;
    private final ShiftCloseMapper shiftCloseMapper;

    public ShiftCloseService() {
        this.shiftCloseMapper = new ShiftCloseMapper();
        this.repository = new ShiftCloseRepositoryImpl();
    }

    public ShiftCloseDTO saveShiftClose(ShiftCloseDTO shiftCloseDTO) {
        ShiftClose shiftClose = shiftCloseMapper.toShiftClose(shiftCloseDTO);
        return doInTransaction(em -> shiftCloseMapper.toShiftCloseDTO(repository.save(em, shiftClose)));
    }


    // Tìm ShiftClose theo ID
    public ShiftClose getShiftCloseById(Long id) {
        if (id == null) return null;
//        return repository.findById(id);
        return doInTransaction(em -> repository.findById(em, id));
    }

    // Cập nhật ShiftClose
    public ShiftClose updateShiftClose(ShiftClose shiftClose) {
        if (shiftClose == null || shiftClose.getShiftCloseId() == null) {
            throw new IllegalArgumentException("ShiftClose hoặc shiftCloseId không được null");
        }
        if (shiftClose.getDifference() == null
                && shiftClose.getCashInDrawer() != null
                && shiftClose.getTotalRevenue() != null) {

            BigDecimal moneyOpenShift = new BigDecimal("5000000");

            // Tiền chênh lệch = Tiền trong két - (Doanh thu + 5,000,000)
            BigDecimal difference = shiftClose.getCashInDrawer()
                    .subtract(shiftClose.getTotalRevenue().add(moneyOpenShift));
            shiftClose.setDifference(difference);
        }

//        return repository.update(shiftClose);
        return doInTransaction(em -> repository.update(em, shiftClose));
    }

    // Xóa ShiftClose theo ID
    public void deleteShiftClose(Long id) {
        if (id == null) return;
//        repository.deleteById(id);
        doInTransactionVoid(em -> repository.deleteById(em, id));
    }

    // Lấy tất cả ShiftClose
    public List<ShiftClose> getAllShiftClose() {
//        return repository.findAll();
        return doInTransaction(repository::findAll);
    }

    public List<ShiftCloseDTO> getShiftCloseByEmployeeShift(Long employeeShiftId) {

        return doInTransaction(em -> repository.findByEmployeeShift(em, employeeShiftId)).stream()
                .map(shiftCloseMapper::toShiftCloseDTO)
                .toList()
                ;
    }

    public BigDecimal getTotalCashRevenueForShift(Long employeeShiftId) {
//        return repository.getTotalCashRevenueForShift(employeeShiftId);
        return doInTransaction(em -> repository.getTotalCashRevenueForShift(em, employeeShiftId));
    }
}

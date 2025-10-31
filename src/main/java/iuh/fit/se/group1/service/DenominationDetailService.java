/**
 * @ (#) DenominationDetailService.java   1.0     31/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.service;


import iuh.fit.se.group1.entity.DenominationDetail;
import iuh.fit.se.group1.repository.DenominationDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet 
 * @version: 1.0
 * @created: 31/10/2025
 */

public class DenominationDetailService {
    private static final Logger log = LoggerFactory.getLogger(DenominationDetailService.class);
    private final DenominationDetailRepository repository;

    public DenominationDetailService() {
        this.repository = new DenominationDetailRepository();
    }

    /**
     * Lưu mới một chi tiết mệnh giá
     */
    public DenominationDetail save(DenominationDetail detail) {
        if (detail == null) {
            throw new IllegalArgumentException("DenominationDetail cannot be null");
        }
        if (detail.getEmployeeShift() == null) {
            throw new IllegalArgumentException("EmployeeShift cannot be null");
        }
        return repository.save(detail);
    }

    /**
     * Cập nhật thông tin mệnh giá
     */
    public DenominationDetail update(DenominationDetail detail) {
        if (detail == null || detail.getDenominationDetailId() == null) {
            throw new IllegalArgumentException("Invalid DenominationDetail to update");
        }
        return repository.update(detail);
    }

    /**
     * Xóa theo ID
     */
    public void deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
        repository.deleteById(id);
    }

    /**
     * Tìm tất cả
     */
    public List<DenominationDetail> findAll() {
        return repository.findAll();
    }

    /**
     * Tìm theo ID
     */
    public DenominationDetail findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
        return repository.findById(id);
    }

    /**
     * Tìm tất cả mệnh giá theo employeeShiftId
     */
    public List<DenominationDetail> findByEmployeeShiftId(Long employeeShiftId) {
        if (employeeShiftId == null || employeeShiftId <= 0) {
            throw new IllegalArgumentException("Invalid employeeShiftId");
        }
        return findByEmployeeShiftId(employeeShiftId);
    }
}

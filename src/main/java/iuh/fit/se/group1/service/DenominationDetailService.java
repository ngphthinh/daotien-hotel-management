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

import java.util.Arrays;
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

    // Các mệnh giá tiền mặc định (VND)
    private static final List<Long> DEFAULT_DENOMINATIONS = Arrays.asList(
            500000L, 200000L, 100000L, 50000L, 20000L, 10000L, 5000L, 2000L, 1000L
    );

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
     * ĐÃ SỬA: Gọi repository thay vì đệ quy
     */
    public List<DenominationDetail> findByEmployeeShiftId(Long employeeShiftId) {
        if (employeeShiftId == null || employeeShiftId <= 0) {
            throw new IllegalArgumentException("Invalid employeeShiftId");
        }
        return repository.findByEmployeeShiftId(employeeShiftId);
    }

    /**
     * Lấy danh sách các mệnh giá tiền có sẵn
     * Load từ database, nếu không có thì dùng mặc định
     */
    public List<Long> getAvailableDenominations() {
        try {
            List<Long> dbDenominations = repository.findAllDistinctDenominations();

            if (dbDenominations != null && !dbDenominations.isEmpty()) {
                log.info("Loaded {} denominations from database", dbDenominations.size());
                return dbDenominations;
            }

            log.info("No denominations in database, using default values");
            return DEFAULT_DENOMINATIONS;

        } catch (Exception e) {
            log.error("Error loading denominations from database, using defaults", e);
            return DEFAULT_DENOMINATIONS;
        }
    }

    /**
     * Lấy danh sách mệnh giá mặc định
     */
    public List<Long> getDefaultDenominations() {
        return DEFAULT_DENOMINATIONS;
    }
}
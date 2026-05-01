/**
 * @ (#) DenominationDetailService.java   1.0     31/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.DenominationDetailDTO;
import iuh.fit.se.group1.entity.DenominationDetail;
import iuh.fit.se.group1.mapper.DenominationDetailMapper;
import iuh.fit.se.group1.repository.jpa.DenominationDetailRepositoryImpl;
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

public class DenominationDetailService extends Service {
    private static final Logger log = LoggerFactory.getLogger(DenominationDetailService.class);
    private final DenominationDetailRepositoryImpl repository;
    private final DenominationDetailMapper denominationDetailMapper;
    // Các mệnh giá tiền mặc định (VND)
    private static final List<Long> DEFAULT_DENOMINATIONS = Arrays.asList(
            500000L, 200000L, 100000L, 50000L, 20000L, 10000L, 5000L, 2000L, 1000L
    );

    public DenominationDetailService() {
        this.repository = new DenominationDetailRepositoryImpl();
        this.denominationDetailMapper = new DenominationDetailMapper();
    }

    public void saveAll(List<DenominationDetailDTO> details) {
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("Details list cannot be null or empty");
        }
        for (DenominationDetailDTO detail : details) {
            if (detail.getEmployeeShift() == null) {
                throw new IllegalArgumentException("EmployeeShift cannot be null for detail: " + detail);
            }
        }
        List<DenominationDetail> detailsEntity = details.stream().map(denominationDetailMapper::toDenominationDetail).toList();
//        repository.saveBatch(details);
        doInTransactionVoid(em -> {
            repository.saveBatch(em, detailsEntity);
        });
    }

    /**
     * Lưu mới một chi tiết mệnh giá
     */
    public DenominationDetailDTO save(DenominationDetailDTO detail) {
        if (detail == null) {
            throw new IllegalArgumentException("DenominationDetail cannot be null");
        }
        if (detail.getEmployeeShift() == null) {
            throw new IllegalArgumentException("EmployeeShift cannot be null");
        }
        DenominationDetail entity = denominationDetailMapper.toDenominationDetail(detail);
//        return repository.save(detail);
        return doInTransaction(em -> denominationDetailMapper.toDTO(repository.save(em, entity)));
    }

    /**
     * Cập nhật thông tin mệnh giá
     */
    public DenominationDetailDTO update(DenominationDetailDTO detail) {
        if (detail == null || detail.getDenominationDetailId() == null) {
            throw new IllegalArgumentException("Invalid DenominationDetail to update");
        }
        DenominationDetail entity = denominationDetailMapper.toDenominationDetail(detail);
//        return repository.update(detail);
        return doInTransaction(em -> denominationDetailMapper.toDTO(repository.update(em, entity)));
    }

    /**
     * Xóa theo ID
     */
    public void deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
//        repository.deleteById(id);
        doInTransactionVoid(em -> repository.deleteById(em, id));
    }

    /**
     * Tìm tất cả
     */
    public List<DenominationDetailDTO> findAll() {
//        return repository.findAll();
        return doInTransaction(repository::findAll).stream()
                .map(denominationDetailMapper::toDTO)
                .toList();
    }

    /**
     * Tìm theo ID
     */
    public DenominationDetailDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
//        return repository.findById(id);
        return doInTransaction(em -> denominationDetailMapper.toDTO(repository.findById(em, id)));
    }

    /**
     * Tìm tất cả mệnh giá theo employeeShiftId
     * ĐÃ SỬA: Gọi repository thay vì đệ quy
     */
    public List<DenominationDetail> findByEmployeeShiftId(Long employeeShiftId) {
        if (employeeShiftId == null || employeeShiftId <= 0) {
            throw new IllegalArgumentException("Invalid employeeShiftId");
        }
//        return repository.findByEmployeeShiftId(employeeShiftId);
        return doInTransaction(em -> repository.findByEmployeeShiftId(em, employeeShiftId));
    }

    /**
     * Lấy danh sách các mệnh giá tiền có sẵn
     * Load từ database, nếu không có thì dùng mặc định
     */
    public List<Long> getAvailableDenominations() {
        try {
//            List<Long> dbDenominations = repository.findAllDistinctDenominations();
            List<Long> dbDenominations = doInTransaction(repository::findAllDistinctDenominations);

            if (dbDenominations != null && !dbDenominations.isEmpty()) {
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
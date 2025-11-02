/**
 * @ (#) DenominationDetailRepository.java   1.0     31/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.repository;


import iuh.fit.se.group1.entity.DenominationDetail;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.enums.DenominationLabel;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet 
 * @version: 1.0
 * @created: 31/10/2025
 */

public class DenominationDetailRepository implements Repository<DenominationDetail, Long> {
    private static final Logger log = LoggerFactory.getLogger(DenominationDetailRepository.class);
    private final Connection connection;

    public DenominationDetailRepository() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public DenominationDetail save(DenominationDetail entity) {
        String sql = "INSERT INTO DenominationDetail (denomination, quantity, employeeShiftId, createdAt) VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getDenomination().name());
            ps.setInt(2, entity.getQuantity());
            ps.setLong(3, entity.getEmployeeShift().getEmployeeShiftId());
            ps.setDate(4, Date.valueOf(entity.getCreatedAt()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setDenominationDetailId(generatedKeys.getLong(1));
                }
            }

            return entity;

        } catch (SQLException e) {
            log.error("Error saving DenominationDetail: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public DenominationDetail findById(Long id) {
        String sql = "SELECT * FROM DenominationDetail WHERE denominationDetailId = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DenominationDetail detail = new DenominationDetail();
                    detail.setDenominationDetailId(rs.getLong("denominationDetailId"));
                    detail.setDenomination(DenominationLabel.fromName(rs.getString("denomination")));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setCreatedAt(rs.getDate("createdAt").toLocalDate());

                    EmployeeShift es = new EmployeeShift();
                    es.setEmployeeShiftId(rs.getLong("employeeShiftId"));
                    detail.setEmployeeShift(es);

                    return detail;
                }
            }

            return null;

        } catch (SQLException e) {
            log.error("Error finding DenominationDetail by ID: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM DenominationDetail WHERE denominationDetailId = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete failed, no rows affected.");
            }
        } catch (SQLException e) {
            log.error("Error deleting DenominationDetail: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DenominationDetail> findAll() {
        List<DenominationDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM DenominationDetail;";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DenominationDetail detail = new DenominationDetail();
                detail.setDenominationDetailId(rs.getLong("denominationDetailId"));
                detail.setDenomination(DenominationLabel.fromName(rs.getString("denomination")));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setCreatedAt(rs.getDate("createdAt").toLocalDate());

                EmployeeShift es = new EmployeeShift();
                es.setEmployeeShiftId(rs.getLong("employeeShiftId"));
                detail.setEmployeeShift(es);

                list.add(detail);
            }

            return list;

        } catch (SQLException e) {
            log.error("Error finding all DenominationDetails: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public DenominationDetail update(DenominationDetail entity) {
        String sql = "UPDATE DenominationDetail SET denomination=?, quantity=?, employeeShiftId=? WHERE denominationDetailId=?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getDenomination().name());
            ps.setInt(2, entity.getQuantity());
            ps.setLong(3, entity.getEmployeeShift().getEmployeeShiftId());
            ps.setLong(4, entity.getDenominationDetailId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            return entity;

        } catch (SQLException e) {
            log.error("Error updating DenominationDetail: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Find all denomination details by employee shift
     */
    public List<DenominationDetail> findByEmployeeShiftId(Long employeeShiftId) {
        List<DenominationDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM DenominationDetail WHERE employeeShiftId = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, employeeShiftId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DenominationDetail detail = new DenominationDetail();
                    detail.setDenominationDetailId(rs.getLong("denominationDetailId"));
                    detail.setDenomination(DenominationLabel.fromName(rs.getString("denomination")));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setCreatedAt(rs.getDate("createdAt").toLocalDate());

                    EmployeeShift es = new EmployeeShift();
                    es.setEmployeeShiftId(rs.getLong("employeeShiftId"));
                    detail.setEmployeeShift(es);

                    list.add(detail);
                }
            }

            return list;

        } catch (SQLException e) {
            log.error("Error finding DenominationDetails by EmployeeShift: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Save multiple denomination details in batch
     */
    public void saveBatch(List<DenominationDetail> details) {
        String sql = "INSERT INTO DenominationDetail (denomination, quantity, employeeShiftId, createdAt) VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (DenominationDetail detail : details) {
                ps.setString(1, detail.getDenomination().name());
                ps.setInt(2, detail.getQuantity());
                ps.setLong(3, detail.getEmployeeShift().getEmployeeShiftId());
                ps.setDate(4, Date.valueOf(detail.getCreatedAt()));
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            log.error("Error saving batch DenominationDetails: ", e);
            throw new RuntimeException(e);
        }
    }
    // Thêm vào DenominationDetailRepository
    /**
     * Lấy danh sách các mệnh giá DISTINCT từ database (sử dụng JDBC)
     */
    public List<Long> findAllDistinctDenominations() {
        List<Long> denominations = new ArrayList<>();
        String sql = "SELECT DISTINCT denomination FROM DenominationDetail ORDER BY denomination DESC;";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String denomName = rs.getString("denomination");
                DenominationLabel label = DenominationLabel.fromName(denomName);
                if (label != null) {
                    denominations.add(label.getValue());
                }
            }

            return denominations;

        } catch (SQLException e) {
            log.error("Error finding distinct denominations: ", e);
            return null;
        }
    }
}

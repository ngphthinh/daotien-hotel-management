/**
 * @ (#) ShiftCloseRepository.java   1.0     31/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.repository;


import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.entity.ShiftClose;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet 
 * @version: 1.0
 * @created: 31/10/2025
 */

public class ShiftCloseRepository implements Repository<ShiftClose,Long>{
    private static final Logger log = LoggerFactory.getLogger(ShiftCloseRepository.class);
    private final Connection connection;

    public ShiftCloseRepository() {
        connection = DatabaseUtil.getConnection();
    }
    // Thay thế phương thức save trong ShiftCloseRepository.java

    @Override
    public ShiftClose save(ShiftClose entity) {
        if (entity.getTotalRevenue() != null && entity.getCashInDrawer() != null) {
            BigDecimal moneyOpenShift = new BigDecimal("5000000"); // 5 triệu đầu ca
            BigDecimal difference = entity.getCashInDrawer()
                    .subtract(entity.getTotalRevenue().add(moneyOpenShift));
            entity.setDifference(difference);
        } else {
            entity.setDifference(BigDecimal.ZERO);
        }

        String sql = "INSERT INTO ShiftClose(employeeShiftId, totalRevenue, cashInDrawer, difference, note, createdAt) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, entity.getEmployeeShift().getEmployeeShiftId());
            ps.setBigDecimal(2, entity.getTotalRevenue());
            ps.setBigDecimal(3, entity.getCashInDrawer());
            ps.setBigDecimal(4, entity.getDifference());
            ps.setString(5, entity.getNote());
            ps.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt() != null ? entity.getCreatedAt() : LocalDateTime.now()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setShiftCloseId(generatedKeys.getLong(1));
                }
            }

            return entity;

        } catch (SQLException e) {
            log.error("Error saving ShiftClose: ", e);
            throw new RuntimeException(e);
        }
    }
    @Override
    public ShiftClose findById(Long id) {
        String sql = "SELECT * FROM ShiftClose WHERE shiftCloseId = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ShiftClose sc = new ShiftClose();
                    sc.setShiftCloseId(rs.getLong("shiftCloseId"));

                    EmployeeShift es = new EmployeeShift();
                    es.setEmployeeShiftId(rs.getLong("employeeShiftId"));
                    sc.setEmployeeShift(es);

                    sc.setTotalRevenue(rs.getBigDecimal("totalRevenue"));
                    sc.setCashInDrawer(rs.getBigDecimal("cashInDrawer") );
                    sc.setDifference(rs.getBigDecimal("difference"));
                    sc.setNote(rs.getString("note"));
                    sc.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());

                    return sc;
                }
            }

            return null;

        } catch (SQLException e) {
            log.error("Error finding ShiftClose by ID: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM ShiftClose WHERE shiftCloseId = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete failed, no rows affected.");
            }
        } catch (SQLException e) {
            log.error("Error deleting ShiftClose: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ShiftClose> findAll() {
        List<ShiftClose> list = new ArrayList<>();
        String sql = "SELECT * FROM ShiftClose;";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ShiftClose sc = new ShiftClose();
                sc.setShiftCloseId(rs.getLong("shiftCloseId"));

                EmployeeShift es = new EmployeeShift();
                es.setEmployeeShiftId(rs.getLong("employeeShiftId"));
                sc.setEmployeeShift(es);

                sc.setTotalRevenue(rs.getBigDecimal("totalRevenue") );
                sc.setCashInDrawer(rs.getBigDecimal("cashInDrawer") );
                sc.setDifference(rs.getBigDecimal("difference") );
                sc.setNote(rs.getString("note"));
                sc.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());

                list.add(sc);
            }

            return list;

        } catch (SQLException e) {
            log.error("Error finding all ShiftClose: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ShiftClose update(ShiftClose entity) {
        String sql = "UPDATE ShiftClose SET employeeShiftId=?, totalRevenue=?, cashInDrawer=?, difference=?, note=? WHERE shiftCloseId=?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getEmployeeShift().getEmployeeShiftId());
            ps.setBigDecimal(2, entity.getTotalRevenue());
            ps.setBigDecimal(3,entity.getCashInDrawer());
            ps.setBigDecimal(4, entity.getDifference());
            ps.setString(5, entity.getNote());
            ps.setLong(6, entity.getShiftCloseId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            return entity;

        } catch (SQLException e) {
            log.error("Error updating ShiftClose: ", e);
            throw new RuntimeException(e);
        }
    }

    public List<ShiftClose> findByEmployeeShift(EmployeeShift employeeShift) {
        List<ShiftClose> list = new ArrayList<>();
        String sql = "SELECT * FROM ShiftClose WHERE employeeShiftId=?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, employeeShift.getEmployeeShiftId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShiftClose sc = new ShiftClose();
                    sc.setShiftCloseId(rs.getLong("shiftCloseId"));

                    EmployeeShift es = new EmployeeShift();
                    es.setEmployeeShiftId(rs.getLong("employeeShiftId"));
                    sc.setEmployeeShift(es);

                    sc.setTotalRevenue(rs.getBigDecimal("totalRevenue"));
                    sc.setCashInDrawer(rs.getBigDecimal("cashInDrawer") );
                    sc.setDifference(rs.getBigDecimal("difference") );
                    sc.setNote(rs.getString("note"));
                    sc.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());

                    list.add(sc);
                }
            }

            return list;

        } catch (SQLException e) {
            log.error("Error finding ShiftClose by EmployeeShift: ", e);
            throw new RuntimeException(e);
        }
    }
}

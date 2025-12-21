/**
 * @ (#) ShiftCloseRepository.java   1.0     31/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.entity.ShiftClose;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import iuh.fit.se.group1.util.PasswordUtil;
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

        String sql = "INSERT INTO ShiftClose(employeeShiftId, totalRevenue, cashInDrawer, " +
                "difference, note, managerId, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, entity.getEmployeeShift().getEmployeeShiftId());
            ps.setBigDecimal(2, entity.getTotalRevenue());
            ps.setBigDecimal(3, entity.getCashInDrawer());
            ps.setBigDecimal(4, entity.getDifference());
            ps.setString(5, entity.getNote());
            ps.setLong(6, entity.getManagerId());
            ps.setTimestamp(7, Timestamp.valueOf(entity.getCreatedAt() != null ?
                    entity.getCreatedAt() : LocalDateTime.now()));
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
                    sc.setManagerId(rs.getLong("managerId"));
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
                sc.setManagerId(rs.getLong("managerId"));
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
        String sql = "UPDATE ShiftClose SET employeeShiftId=?, totalRevenue=?, cashInDrawer=?, " +
                "difference=?, note=?, managerId=? WHERE shiftCloseId=?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getEmployeeShift().getEmployeeShiftId());
            ps.setBigDecimal(2, entity.getTotalRevenue());
            ps.setBigDecimal(3,entity.getCashInDrawer());
            ps.setBigDecimal(4, entity.getDifference());
            ps.setString(5, entity.getNote());
            ps.setLong(6, entity.getManagerId());
            ps.setLong(7, entity.getShiftCloseId());

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
                    sc.setManagerId(rs.getLong("managerId"));
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
    public BigDecimal getTotalCashRevenueForShift(Long employeeShiftId) {
        String sql = """
        SELECT COALESCE(SUM(O.totalAmount), 0) as totalCashRevenue
        FROM Orders O
        INNER JOIN EmployeeShift ES ON O.employeeId = ES.employeeId
        INNER JOIN Shift S ON ES.shiftId = S.shiftId
        WHERE ES.employeeShiftId = ?
        AND CAST(O.orderDate AS DATE) = ES.shiftDate
        AND O.orderTypeId = 1
        AND O.paymentType = 'CASH'
        AND (
            -- Xử lý ca bình thường (không qua đêm)
            (S.startTime < S.endTime 
                AND CAST(O.orderDate AS TIME) >= CAST(S.startTime AS TIME)
                AND CAST(O.orderDate AS TIME) <= CAST(S.endTime AS TIME))
            OR
            -- Xử lý ca qua đêm (startTime > endTime, ví dụ: 18:00 - 23:59:59)
            (S.startTime >= S.endTime
                AND CAST(O.orderDate AS TIME) >= CAST(S.startTime AS TIME))
        )
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, employeeShiftId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("totalCashRevenue");
                }
                return BigDecimal.ZERO;
            }

        } catch (SQLException e) {
            log.error("Error calculating total CASH revenue for shift: ", e);
            throw new RuntimeException(e);
        }
    }
    public Employee validateManager(String username, String password) {
        // Query lấy thông tin manager và password hash
        String sql = "SELECT e.*, a.password as hashedPassword " +
                "FROM Employee e " +
                "INNER JOIN Account a ON e.accountId = a.accountId " +
                "WHERE a.username = ? AND a.roleId = 'MANAGER';";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("hashedPassword");

                    // ✅ DÙNG PasswordUtil.checkPassword() ĐỂ XÁC THỰC
                    if (PasswordUtil.checkPassword(password, hashedPassword)) {
                        // Password đúng - Tạo Employee object
                        Employee manager = new Employee();
                        manager.setEmployeeId(rs.getLong("employeeId"));
                        manager.setFullName(rs.getString("fullName"));
                        manager.setPhone(rs.getString("phone"));
                        manager.setEmail(rs.getString("email"));

                        // Set thêm các field khác nếu cần
                        try {
                            manager.setHireDate(rs.getDate("hireDate").toLocalDate());
                        } catch (Exception e) {
                            log.warn("hireDate is null or invalid");
                        }

                        try {
                            manager.setCitizenId(rs.getString("citizenId"));
                        } catch (Exception e) {
                            log.warn("citizenId is null");
                        }

                        try {
                            manager.setGender(rs.getBoolean("gender"));
                        } catch (Exception e) {
                            log.warn("gender is null");
                        }

                        log.info("Manager validated successfully: {}", username);
                        return manager;

                    } else {
                        log.warn("Wrong password for username: {}", username);
                        return null;
                    }
                } else {
                    log.warn("Username not found or not a manager: {}", username);
                    return null;
                }
            }

        } catch (SQLException e) {
            log.error("Error validating manager: ", e);
            throw new RuntimeException(e);
        }
    }
    public Employee getManagerById(Long managerId) {
        String sql = "SELECT e.* FROM Employee e " +
                "INNER JOIN Account a ON e.accountId = a.accountId " +
                "WHERE e.employeeId = ? AND a.roleId = 'MANAGER';";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, managerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee manager = new Employee();
                    manager.setEmployeeId(rs.getLong("employeeId"));
                    manager.setFullName(rs.getString("fullName"));
                    manager.setPhone(rs.getString("phone"));
                    manager.setEmail(rs.getString("email"));

                    return manager;
                }
            }

            return null;

        } catch (SQLException e) {
            log.error("Error getting manager by ID: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Lấy tên manager theo ID
     */
    public String getManagerNameById(Long managerId) {
        String sql = "SELECT fullName FROM Employee WHERE employeeId = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, managerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("fullName");
                }
            }

            return null;

        } catch (SQLException e) {
            log.error("Error getting manager name: ", e);
            throw new RuntimeException(e);
        }
    }
}

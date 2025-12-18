/**
 * @ (#) EmployeeShiftRepository.java   1.0     30/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.repository;


import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.entity.Shift;
import iuh.fit.se.group1.entity.ShiftClose;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet
 * @version: 1.0
 * @created: 30/10/2025
 */

public class EmployeeShiftRepository implements Repository<EmployeeShift,Long>{
    private static final Logger log = LoggerFactory.getLogger(EmployeeRepository.class);

    private final Connection connection;

    public EmployeeShiftRepository() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public EmployeeShift save(EmployeeShift entity) {
        String sql = "INSERT INTO EmployeeShift (employeeId, shiftId, createdAt, shiftDate) VALUES (?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, entity.getEmployee().getEmployeeId());
            preparedStatement.setLong(2, entity.getShift().getShiftId());


            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDate.now());
            }
            preparedStatement.setDate(3, Date.valueOf(entity.getCreatedAt()));

            if (entity.getShiftDate() == null) {
                entity.setShiftDate(LocalDate.now());
            }
            preparedStatement.setDate(4, Date.valueOf(entity.getShiftDate()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setEmployeeShiftId(generatedKeys.getLong(1));
                }
            }
            return entity;
        } catch (SQLException e) {
            log.error("Error saving EmployeeShift: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public EmployeeShift findById(Long aLong) {
        String sql = "SELECT * FROM EmployeeShift WHERE employeeShiftId = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    EmployeeShift es = new EmployeeShift();
                    es.setEmployeeShiftId(resultSet.getLong("employeeShiftId"));

                    Employee emp = new Employee();
                    emp.setEmployeeId(resultSet.getLong("employeeId"));
                    es.setEmployee(emp);

                    Shift shift = new Shift();
                    shift.setShiftId(resultSet.getLong("shiftId"));
                    es.setShift(shift);
                    es.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    es.setShiftDate(resultSet.getDate("shiftDate").toLocalDate());
                    return es;
                }
            }
            return null;
        } catch (SQLException e) {
            log.error("Error finding EmployeeShift by ID: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        String sql = "DELETE FROM EmployeeShift WHERE employeeShiftId = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete failed, no rows affected.");
            }
        } catch (SQLException e) {
            log.error("Error deleting EmployeeShift: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EmployeeShift> findAll() {
        List<EmployeeShift> list = new ArrayList<>();
        String sql = """
        SELECT es.employeeShiftId, es.shiftDate, es.createdAt,
               e.employeeId, e.fullName,
               s.shiftId, s.name, s.startTime, s.endTime
        FROM EmployeeShift es
        JOIN Employee e ON es.employeeId = e.employeeId
        JOIN Shift s ON es.shiftId = s.shiftId
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EmployeeShift es = new EmployeeShift();
                es.setEmployeeShiftId(rs.getLong("employeeShiftId"));
                es.setShiftDate(rs.getDate("shiftDate").toLocalDate());
                es.setCreatedAt(rs.getDate("createdAt").toLocalDate());

                Employee emp = new Employee();
                emp.setEmployeeId(rs.getLong("employeeId"));
                emp.setFullName(rs.getString("fullName"));
                es.setEmployee(emp);

                Shift shift = new Shift();
                shift.setShiftId(rs.getLong("shiftId"));
                shift.setName(rs.getString("name"));
                shift.setStartTime(rs.getString("startTime"));
                shift.setEndTime(rs.getString("endTime"));
                es.setShift(shift);

                list.add(es);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public EmployeeShift update(EmployeeShift entity) {
        String sql = "UPDATE EmployeeShift SET employeeId=?, shiftId=?, createdAt=?, shiftDate=? WHERE employeeShiftId=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, entity.getEmployee().getEmployeeId());
            preparedStatement.setLong(2, entity.getShift().getShiftId());
            preparedStatement.setDate(3, Date.valueOf(entity.getCreatedAt()));
            preparedStatement.setDate(4, Date.valueOf(entity.getShiftDate()));
            preparedStatement.setLong(5, entity.getEmployeeShiftId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
            return entity;
        } catch (SQLException e) {
            log.error("Error updating EmployeeShift: ", e);
            throw new RuntimeException(e);
        }
    }
    public List<EmployeeShift> findByShiftDate(LocalDate date) {
        List<EmployeeShift> list = new ArrayList<>();

        String sql = """
        SELECT es.employeeShiftId, es.shiftDate, es.createdAt,
               e.employeeId, e.fullName, e.avt,
               s.shiftId, s.name AS shiftName, s.startTime, s.endTime
        FROM EmployeeShift es
        JOIN Employee e ON es.employeeId = e.employeeId
        JOIN Shift s ON es.shiftId = s.shiftId
        WHERE es.shiftDate = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EmployeeShift es = new EmployeeShift();
                    es.setEmployeeShiftId(rs.getLong("employeeShiftId"));
                    es.setShiftDate(rs.getDate("shiftDate").toLocalDate());
                    es.setCreatedAt(rs.getDate("createdAt").toLocalDate());

                    // ===== EMPLOYEE =====
                    Employee emp = new Employee();
                    emp.setEmployeeId(rs.getLong("employeeId"));
                    emp.setFullName(rs.getString("fullName"));
                    emp.setAvt(rs.getBytes("avt"));
                    es.setEmployee(emp);

                    // ===== SHIFT =====
                    Shift shift = new Shift();
                    shift.setShiftId(rs.getLong("shiftId"));
                    shift.setName(rs.getString("shiftName"));
                    shift.setStartTime(rs.getString("startTime"));
                    shift.setEndTime(rs.getString("endTime"));
                    es.setShift(shift);

                    list.add(es);
                }
            }
        } catch (SQLException e) {
            log.error("Error findByShiftDate", e);
            throw new RuntimeException(e);
        }

        return list;
    }

    /**
     * Tìm EmployeeShift với thông tin đầy đủ của Employee và Shift
     */
    public EmployeeShift findByIdWithDetails(Long employeeShiftId) {
        String sql = """
        SELECT es.employeeShiftId, es.shiftDate, es.createdAt, es.employeeId, es.shiftId,
               e.fullName AS employeeName, e.phone AS employeePhone, e.email AS employeeEmail,
               s.name AS shiftName, s.startTime, s.endTime
        FROM EmployeeShift es
        JOIN Employee e ON es.employeeId = e.employeeId
        JOIN Shift s ON es.shiftId = s.shiftId
        WHERE es.employeeShiftId = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, employeeShiftId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmployeeShift es = new EmployeeShift();
                    es.setEmployeeShiftId(rs.getLong("employeeShiftId"));
                    es.setShiftDate(rs.getDate("shiftDate").toLocalDate());
                    es.setCreatedAt(rs.getDate("createdAt").toLocalDate());

                    // Employee info
                    Employee emp = new Employee();
                    emp.setEmployeeId(rs.getLong("employeeId"));
                    emp.setFullName(rs.getString("employeeName"));
                    emp.setPhone(rs.getString("employeePhone"));
                    emp.setEmail(rs.getString("employeeEmail"));
                    es.setEmployee(emp);

                    // Shift info
                    Shift shift = new Shift();
                    shift.setShiftId(rs.getLong("shiftId"));
                    shift.setName(rs.getString("shiftName"));
                    shift.setStartTime(rs.getString("startTime"));
                    shift.setEndTime(rs.getString("endTime"));
                    es.setShift(shift);

                    return es;
                }
            }
            return null;

        } catch (SQLException e) {
            log.error("Error finding EmployeeShift with details: ", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Lấy tổng doanh thu của ca làm việc từ các hóa đơn
     */
    public BigDecimal getTotalCashRevenueForShift(Long employeeShiftId) {
        String sql = """
        SELECT COALESCE(SUM(O.totalAmount), 0) as totalCashRevenue
        FROM Orders O
        INNER JOIN Booking B ON O.orderId = B.orderId
        WHERE B.employeeShiftId = ?
        AND O.orderTypeId = 1
        AND O.paymentType = 'CASH'
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

}

/**
 * @ (#) EmployeeShiftRepository.java   1.0     30/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.repository;


import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.entity.Shift;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
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
        String sql = "INSERT INTO EmployeeShift (employeeId, shiftId, closingTime, createdAt, shiftDate) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, entity.getEmployee().getEmployeeId());
            preparedStatement.setLong(2, entity.getShift().getShiftId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getClosingTime()));

            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDate.now());
            }
            preparedStatement.setDate(4, Date.valueOf(entity.getCreatedAt()));

            if (entity.getShiftDate() == null) {
                entity.setShiftDate(LocalDate.now());
            }
            preparedStatement.setDate(5, Date.valueOf(entity.getShiftDate()));

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

                    es.setClosingTime(resultSet.getTimestamp("closingTime").toLocalDateTime());
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
        List<EmployeeShift> employeeShifts = new ArrayList<>();
        String sql = "SELECT * FROM EmployeeShift;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                EmployeeShift es = new EmployeeShift();
                es.setEmployeeShiftId(rs.getLong("employeeShiftId"));

                Employee emp = new Employee();
                emp.setEmployeeId(rs.getLong("employeeId"));
                es.setEmployee(emp);

                Shift shift = new Shift();
                shift.setShiftId(rs.getLong("shiftId"));
                es.setShift(shift);

                es.setClosingTime(rs.getTimestamp("closingTime").toLocalDateTime());
                es.setCreatedAt(rs.getDate("createdAt").toLocalDate());
                es.setShiftDate(rs.getDate("shiftDate").toLocalDate());

                employeeShifts.add(es);
            }
        } catch (SQLException e) {
            log.error("Error finding all EmployeeShifts: ", e);
            throw new RuntimeException(e);
        }
        return employeeShifts;
    }

    @Override
    public EmployeeShift update(EmployeeShift entity) {
        String sql = "UPDATE EmployeeShift SET employeeId=?, shiftId=?, closingTime=?, createdAt=?, shiftDate=? WHERE employeeShiftId=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, entity.getEmployee().getEmployeeId());
            preparedStatement.setLong(2, entity.getShift().getShiftId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getClosingTime()));
            preparedStatement.setDate(4, Date.valueOf(entity.getCreatedAt()));
            preparedStatement.setDate(5, Date.valueOf(entity.getShiftDate()));
            preparedStatement.setLong(6, entity.getEmployeeShiftId());

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
        List<EmployeeShift> employeeShifts = new ArrayList<>();
        String sql = "SELECT * FROM EmployeeShift WHERE shiftDate = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    EmployeeShift es = new EmployeeShift();
                    es.setEmployeeShiftId(rs.getLong("employeeShiftId"));

                    Employee emp = new Employee();
                    emp.setEmployeeId(rs.getLong("employeeId"));
                    es.setEmployee(emp);

                    Shift shift = new Shift();
                    shift.setShiftId(rs.getLong("shiftId"));
                    es.setShift(shift);

                    es.setClosingTime(rs.getTimestamp("closingTime").toLocalDateTime());
                    es.setCreatedAt(rs.getDate("createdAt").toLocalDate());
                    es.setShiftDate(rs.getDate("shiftDate").toLocalDate());

                    employeeShifts.add(es);
                }
            }
        } catch (SQLException e) {
            log.error("Error finding EmployeeShifts by shiftDate: ", e);
            throw new RuntimeException(e);
        }
        return employeeShifts;
    }
}

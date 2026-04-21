/**
 * @ (#) ShiftRepository.java   1.0     27/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.repository;


import iuh.fit.se.group1.entity.Shift;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import iuh.fit.se.group1.repository.interfaces.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet
 * @version: 1.0
 * @created: 27/10/2025
 */

public class ShiftRepositoryImpl implements Repository<Shift, Long>, iuh.fit.se.group1.repository.interfaces.ShiftRepository {
    private static final Logger log = LoggerFactory.getLogger(ShiftRepositoryImpl.class);

    private final Connection connection;

    public ShiftRepositoryImpl() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public Shift save(Shift entity) {
        String sql = "Insert into Shift(name,startTime,endTime,createdAt) values (?,?,?,?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getStartTime());
            preparedStatement.setString(3, entity.getEndTime());
            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDate.now());
            }
            preparedStatement.setDate(4, Date.valueOf(entity.getCreatedAt()));
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setShiftId(generatedKeys.getLong(1));
                }
            }
            return entity;
        } catch (SQLException e) {
            log.error("Error saving Promotion: ", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public Shift findById(Long aLong) {
        String sql = "Select * from Shift where shiftId=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Shift shift = new Shift();
                    shift.setShiftId(resultSet.getLong("shiftId"));
                    shift.setName(resultSet.getString("name"));
                    shift.setStartTime(resultSet.getString("startTime"));
                    shift.setEndTime(resultSet.getString("endTime"));
                    shift.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return shift;
                }
            }
            return null;
        } catch (SQLException e) {
            log.error("Error finding Shift by ID: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        String sql = "Delete from Shift where shiftId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete failed, no rows affected.");
            }
        } catch (SQLException e) {
            log.error("Error deleting Shift by ID: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Shift> findAll() {
        List<Shift> shifts = new java.util.ArrayList<>();
        String sql = "Select * from Shift";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Shift shift = new Shift();
                shift.setShiftId(rs.getLong("shiftId"));
                shift.setName(rs.getString("name"));
                shift.setStartTime(rs.getString("startTime"));
                shift.setEndTime(rs.getString("endTime"));
                shift.setCreatedAt(rs.getDate("createdAt").toLocalDate());
                shifts.add(shift);
            }
        } catch (SQLException e) {
            log.error("Error finding all Shifts: ", e);
            throw new RuntimeException(e);
        }
        return shifts;
    }

    @Override
    public Shift update(Shift entity) {
        String sql = "Update Shift set name=?, startTime=?, endTime=? where shiftId=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getStartTime());
            preparedStatement.setString(3, entity.getEndTime());
            preparedStatement.setLong(4, entity.getShiftId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
            return entity;
        } catch (SQLException e) {
            log.error("Error updating Shift: ", e);
            throw new RuntimeException(e);
        }
    }
}

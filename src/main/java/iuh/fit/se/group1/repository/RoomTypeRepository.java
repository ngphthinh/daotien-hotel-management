package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeRepository implements Repository<RoomType, String> {

    private static final Logger log = LoggerFactory.getLogger(RoomTypeRepository.class);
    private final Connection connection;

    public RoomTypeRepository() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public RoomType save(RoomType entity) {
        String sql = "INSERT INTO RoomType (roomTypeId, name, createdAt) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getRoomTypeId());
            preparedStatement.setString(2, entity.getName());
            entity.setCreatedAt(LocalDate.now());
            preparedStatement.setDate(3, Date.valueOf(entity.getCreatedAt()));

            int affectedRows = preparedStatement.executeUpdate(); 
            if (affectedRows > 0) {
                log.info("Saved RoomType with ID: {}", entity.getRoomTypeId());
            }
            return entity;

        } catch (SQLException e) {
            log.error("Error saving RoomType: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoomType update(RoomType entity) {
        String sql = "UPDATE RoomType SET name = ? WHERE roomTypeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getRoomTypeId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
            log.info("Updated RoomType with ID: {}", entity.getRoomTypeId());

            return entity;

        } catch (SQLException e) {
            log.error("Error updating RoomType: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoomType findById(String id) {
        String sql = "SELECT * FROM RoomType WHERE roomTypeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    RoomType roomType = new RoomType();
                    roomType.setRoomTypeId(resultSet.getString("roomTypeId"));
                    roomType.setName(resultSet.getString("name"));
                    roomType.setHourlyRate(resultSet.getBigDecimal("hourlyRate"));
                    roomType.setDailyRate(resultSet.getBigDecimal("dailyRate"));
                    roomType.setOvernightRate(resultSet.getBigDecimal("overnightRate"));
                    roomType.setAdditionalHourRate(resultSet.getBigDecimal("additionalHourRate"));
                    // Fix: Null-safe cho createdAt
                    Date createdAtDate = resultSet.getDate("createdAt");
                    roomType.setCreatedAt(createdAtDate != null ? createdAtDate.toLocalDate() : null);
                    log.debug("Loaded RoomType ID: {}", id);  // Debug log
                    return roomType;
                }
            }

        } catch (SQLException e) {
            log.error("Error finding RoomType by ID: ", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM RoomType WHERE roomTypeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                log.info("Deleted RoomType with ID: {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting RoomType: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RoomType> findAll() {
        List<RoomType> roomTypes = new ArrayList<>();
        String sql = "SELECT * FROM RoomType ORDER BY roomTypeId";  // Thêm ORDER BY

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                RoomType roomType = new RoomType();
                roomType.setRoomTypeId(resultSet.getString("roomTypeId"));
                roomType.setName(resultSet.getString("name"));
                // Fix: Null-safe cho createdAt
                Date createdAtDate = resultSet.getDate("createdAt");
                roomType.setCreatedAt(createdAtDate != null ? createdAtDate.toLocalDate() : null);
                roomTypes.add(roomType);
            }
            log.info("Found {} RoomTypes in total", roomTypes.size());  // Debug log

        } catch (SQLException e) {
            log.error("Error finding all RoomTypes: ", e);
            throw new RuntimeException(e);
        }

        return roomTypes;
    }
    
}
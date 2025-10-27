package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository implements Repository<Room, Long> {

    private static final Logger log = LoggerFactory.getLogger(RoomRepository.class);
    private final Connection connection;

    public RoomRepository() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public Room save(Room entity) {
        String sql = "INSERT INTO Room (roomNumber, roomTypeId, roomStatus, createdAt) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getRoomNumber());
            preparedStatement.setString(2, entity.getRoomType().getRoomTypeId());
            preparedStatement.setString(3, entity.getRoomStatus().name());
            entity.setCreatedAt(LocalDate.now());
            preparedStatement.setDate(4, Date.valueOf(entity.getCreateAt()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setRoomId(generatedKeys.getLong(1));
                }
            }

            return entity;
        } catch (SQLException e) {
            log.error("Error saving Room: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Room update(Room entity) {
        String sql = "UPDATE Room SET roomNumber = ?, roomTypeId = ?, roomStatus = ? WHERE roomId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getRoomNumber());
            preparedStatement.setString(2, entity.getRoomType().getRoomTypeId());
            preparedStatement.setString(3, entity.getRoomStatus().name());
            preparedStatement.setLong(4, entity.getRoomId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            return entity;
        } catch (SQLException e) {
            log.error("Error updating Room: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Room findById(Long id) {
        String sql = "SELECT * FROM Room WHERE roomId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Room room = new Room();
                    room.setRoomId(resultSet.getLong("roomId"));
                    room.setRoomNumber(resultSet.getString("roomNumber"));

                    RoomType roomType = new RoomType();
                    roomType.setRoomTypeId(resultSet.getString("roomTypeId"));
                    room.setRoomType(roomType);

                    String statusValue = resultSet.getString("roomStatus");
                    if (statusValue != null) {
                        room.setRoomStatus(RoomStatus.valueOf(statusValue));
                    }

                    room.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return room;
                }
            }

        } catch (SQLException e) {
            log.error("Error finding Room by ID: ", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Room WHERE roomId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting Room: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomId(resultSet.getLong("roomId"));
                room.setRoomNumber(resultSet.getString("roomNumber"));

                RoomType roomType = new RoomType();
                roomType.setRoomTypeId(resultSet.getString("roomTypeId"));
                room.setRoomType(roomType);

                String statusValue = resultSet.getString("roomStatus");
                if (statusValue != null) {
                    room.setRoomStatus(RoomStatus.valueOf(statusValue));
                }

                room.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                rooms.add(room);
            }

        } catch (SQLException e) {
            log.error("Error finding all Rooms: ", e);
            throw new RuntimeException(e);
        }

        return rooms;
    }
    public List<Room> findByRoomNumberOrId(String keyword) {
    List<Room> rooms = new ArrayList<>();
    String sql = "SELECT * FROM Room WHERE roomNumber COLLATE SQL_Latin1_General_CP1_CI_AS LIKE ? OR CAST(roomId AS NVARCHAR) LIKE ? ORDER BY roomId ASC, roomNumber ASC";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        String likeKeyword = "%" + keyword + "%";
        preparedStatement.setString(1, likeKeyword);
        preparedStatement.setString(2, likeKeyword);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomId(resultSet.getLong("roomId"));
                room.setRoomNumber(resultSet.getString("roomNumber"));
                
                // Lấy RoomTypeId và gán vào RoomType object
                RoomType roomType = new RoomType();
                roomType.setRoomTypeId(resultSet.getString("roomTypeId"));
                room.setRoomType(roomType);

                // Lấy roomStatus (enum)
                String status = resultSet.getString("roomStatus");
                if (status != null) {
                    room.setRoomStatus(RoomStatus.valueOf(status));
                }

                room.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                rooms.add(room);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error finding rooms by name or ID", e);
    }
    return rooms;
    }

}

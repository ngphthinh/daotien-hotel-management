package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class RoomRepository implements Repository<Room, Long> {

    private static final Logger log = LoggerFactory.getLogger(RoomRepository.class);
    private final Connection connection;
    private final RoomTypeRepository roomTypeRepository;  // Inject để load full RoomType

    public RoomRepository() {
        connection = DatabaseUtil.getConnection();
        this.roomTypeRepository = new RoomTypeRepository();  // Tạm new; tốt hơn inject qua DI
    }

    @Override
public Room save(Room room) {
    String sql = "INSERT INTO Room (roomNumber, roomTypeId, createdAt, roomStatus, price) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        conn.setAutoCommit(true);

        ps.setString(1, room.getRoomNumber());

        if (room.getRoomType() != null && room.getRoomType().getRoomTypeId() != null) {
            ps.setString(2, room.getRoomType().getRoomTypeId());
        } else {
            throw new SQLException("RoomTypeId cannot be null!");
        }

        if (room.getCreateAt() == null) {
            room.setCreatedAt(LocalDate.now());
        }
        ps.setDate(3, Date.valueOf(room.getCreateAt()));
        ps.setString(4, room.getRoomStatus().name());
        ps.setBigDecimal(5, room.getPrice());

        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating room failed, no rows affected.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                room.setRoomId(generatedKeys.getLong(1));
            }
        }

        System.out.println(" Room saved: " + room.getRoomNumber());
        return room;
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error saving room: " + e.getMessage(), e);
    }
}

    @Override
public Room update(Room entity) {
    String sql = "UPDATE Room SET roomNumber = ?, roomTypeId = ?, roomStatus = ?, price = ? WHERE roomId = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

        preparedStatement.setString(1, entity.getRoomNumber());
        preparedStatement.setString(2, entity.getRoomTypeId());
        preparedStatement.setString(3, entity.getRoomStatus().toString());
        preparedStatement.setBigDecimal(4, entity.getPrice() != null ? entity.getPrice() : BigDecimal.ZERO);
        preparedStatement.setLong(5, entity.getRoomId());

        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows > 0) {
            log.info("Updated Room ID: {}", entity.getRoomId());
            return entity;
        } else {
            log.warn("No Room found with ID: {}", entity.getRoomId());
            return null;
        }

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
                    
                    // Load full RoomType
                    String typeId = resultSet.getString("roomTypeId");
                    if (typeId != null) {
                        room.setRoomType(roomTypeRepository.findById(typeId));
                    }
                    
                    room.setCreateAt(resultSet.getDate("createdAt") != null ? resultSet.getDate("createdAt").toLocalDate() : null);  // Null-safe
                    setRoomStatusFromString(room, resultSet.getString("roomStatus"));  // Safe enum set
                    room.setPrice(resultSet.getBigDecimal("price"));  // Nếu cột không tồn tại, sẽ null (không crash nếu schema OK)
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
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                log.info("Deleted Room with ID: {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting Room: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room ORDER BY roomId";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomId(resultSet.getLong("roomId"));
                room.setRoomNumber(resultSet.getString("roomNumber"));
                
                // Load full RoomType
                String typeId = resultSet.getString("roomTypeId");
                if (typeId != null) {
                    room.setRoomType(roomTypeRepository.findById(typeId));
                }
                
                room.setCreateAt(resultSet.getDate("createdAt") != null ? resultSet.getDate("createdAt").toLocalDate() : null);
                setRoomStatusFromString(room, resultSet.getString("roomStatus"));
                room.setPrice(resultSet.getBigDecimal("price"));
                rooms.add(room);
            }
            log.info("Found {} rooms in total", rooms.size());  // Log để debug

        } catch (SQLException e) {
            log.error("Error finding all Rooms: ", e);
            throw new RuntimeException(e);
        }

        return rooms;
    }

    // Helper method để set enum safe (tránh IllegalArgumentException)
    private void setRoomStatusFromString(Room room, String statusStr) {
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                room.setRoomStatus(RoomStatus.valueOf(statusStr.toUpperCase()));  // Normalize to UPPERCASE
            } catch (IllegalArgumentException e) {
                log.warn("Invalid RoomStatus '{}', default to AVAILABLE", statusStr);
                room.setRoomStatus(RoomStatus.AVAILABLE);  // Default fallback
            }
        } else {
            room.setRoomStatus(RoomStatus.AVAILABLE);  // Default nếu null
        }
    }

public List<Room> findByRoomNumberOrId(String keyword) {
    List<Room> rooms = new ArrayList<>();
    String sql = "SELECT * FROM Room WHERE roomNumber LIKE ? OR CAST(roomId AS NVARCHAR(50)) LIKE ? ORDER BY roomId ASC, roomNumber ASC";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        String likeKeyword = "%" + keyword + "%";
        preparedStatement.setString(1, likeKeyword);
        preparedStatement.setString(2, likeKeyword);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomId(resultSet.getLong("roomId"));
                room.setRoomNumber(resultSet.getString("roomNumber"));

                // Load full RoomType
                String typeId = resultSet.getString("roomTypeId");
                if (typeId != null) {
                    room.setRoomType(roomTypeRepository.findById(typeId));
                }

                setRoomStatusFromString(room, resultSet.getString("roomStatus"));
                room.setCreateAt(resultSet.getDate("createdAt") != null
                        ? resultSet.getDate("createdAt").toLocalDate()
                        : null);
                room.setPrice(resultSet.getBigDecimal("price"));
                rooms.add(room);
            }
        }
        log.info("Found {} rooms by keyword '{}'", rooms.size(), keyword);

    } catch (SQLException e) {
        e.printStackTrace(); // 👉 In lỗi thật ra console
        log.error("Error finding rooms by keyword: ", e);
        throw new RuntimeException("Error finding rooms by name or ID", e);
    }
    return rooms;
}
}
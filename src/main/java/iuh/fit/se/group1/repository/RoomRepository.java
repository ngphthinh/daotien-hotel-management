package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.config.AppLogger;
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

@SuppressWarnings("unused")
public class RoomRepository implements Repository<Room, Long> {

    private static final Logger log = LoggerFactory.getLogger(RoomRepository.class);
    private final Connection connection;
    private final RoomTypeRepository roomTypeRepository;

    public RoomRepository() {
        this.connection = DatabaseUtil.getConnection();
        this.roomTypeRepository = new RoomTypeRepository();
    }

    @Override
    public Room save(Room room) {
        String sql = "INSERT INTO Room (roomNumber, roomTypeId, createdAt, roomStatus) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, room.getRoomNumber());

            if (room.getRoomType() != null && room.getRoomType().getRoomTypeId() != null) {
                preparedStatement.setString(2, room.getRoomType().getRoomTypeId());
            } else {
                throw new SQLException("RoomTypeId cannot be null!");
            }

            if (room.getCreateAt() == null) {
                room.setCreatedAt(LocalDate.now());
            }
            preparedStatement.setDate(3, Date.valueOf(room.getCreateAt()));
            preparedStatement.setString(4, room.getRoomStatus().name());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating room failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    room.setRoomId(generatedKeys.getLong(1));
                }
            }

            log.info("Room saved: {}", room.getRoomNumber());
            return room;
        } catch (SQLException e) {
            log.error("Error saving room: ", e);
            throw new RuntimeException("Error saving room: " + e.getMessage(), e);
        }
    }

    @Override
    public Room update(Room entity) {
        String sql = "UPDATE Room SET roomNumber = ?, roomTypeId = ?, roomStatus = ? WHERE roomId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getRoomNumber());

            if (entity.getRoomType() != null && entity.getRoomType().getRoomTypeId() != null) {
                preparedStatement.setString(2, entity.getRoomType().getRoomTypeId());
            } else {
                throw new SQLException("RoomTypeId cannot be null!");
            }

            preparedStatement.setString(3, entity.getRoomStatus().toString());
            preparedStatement.setLong(4, entity.getRoomId());

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
                        RoomType roomType = roomTypeRepository.findById(typeId);
                        room.setRoomType(roomType);
                    }

                    Date createdDate = resultSet.getDate("createdAt");
                    if (createdDate != null) {
                        room.setCreatedAt(createdDate.toLocalDate());
                    }

                    room.setRoomStatus(RoomStatus.fromString(resultSet.getString("roomStatus")));
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
            } else {
                log.warn("No Room found with ID: {}", id);
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
                    RoomType roomType = roomTypeRepository.findById(typeId);
                    room.setRoomType(roomType);
                }

                Date createdDate = resultSet.getDate("createdAt");
                if (createdDate != null) {
                    room.setCreatedAt(createdDate.toLocalDate());
                }

                room.setRoomStatus(RoomStatus.fromString(resultSet.getString("roomStatus")));
                rooms.add(room);
            }
            log.info("Found {} rooms in total", rooms.size());

        } catch (SQLException e) {
            log.error("Error finding all Rooms: ", e);
            throw new RuntimeException(e);
        }

        return rooms;
    }

    public List<Room> findByRoomNumberOrId(String keyword) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE roomNumber LIKE ? OR CAST(roomId AS VARCHAR) LIKE ? ORDER BY roomId ASC, roomNumber ASC";

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
                        RoomType roomType = roomTypeRepository.findById(typeId);
                        room.setRoomType(roomType);
                    }

                    Date createdDate = resultSet.getDate("createdAt");
                    if (createdDate != null) {
                        room.setCreatedAt(createdDate.toLocalDate());
                    }

                    room.setRoomStatus(RoomStatus.fromString(resultSet.getString("roomStatus")));
                    rooms.add(room);
                }
            }
            log.info("Found {} rooms by keyword '{}'", rooms.size(), keyword);

        } catch (SQLException e) {
            log.error("Error finding rooms by keyword: ", e);
            throw new RuntimeException("Error finding rooms by name or ID", e);
        }
        return rooms;
    }

    public Room findRoomByRoomTypeAndStatusAndOverNights(String roomTypeId, RoomStatus roomStatus) {
        // TODO: Implement this method based on your business logic
        return null;
    }

    public List<Room> findRoomByStatusAndRoomType(String roomTypeId, RoomStatus roomStatus) {
        String sql = """
                SELECT r.roomId, r.roomNumber, r.roomTypeId, r.roomStatus, r.createdAt, rt.name
                FROM Room r 
                JOIN RoomType rt ON r.roomTypeId = rt.roomTypeId
                WHERE rt.roomTypeId = ? AND r.roomStatus = ?
                ORDER BY r.roomId ASC
                """;

        List<Room> rooms = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, roomTypeId);
            preparedStatement.setString(2, roomStatus.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Room room = new Room();
                    room.setRoomId(resultSet.getLong("roomId"));
                    room.setRoomNumber(resultSet.getString("roomNumber"));

                    RoomType roomType = new RoomType();
                    roomType.setRoomTypeId(resultSet.getString("roomTypeId"));
                    roomType.setName(resultSet.getString("name"));
                    room.setRoomType(roomType);

                    Date createdDate = resultSet.getDate("createdAt");
                    if (createdDate != null) {
                        room.setCreatedAt(createdDate.toLocalDate());
                    }

                    room.setRoomStatus(RoomStatus.fromString(resultSet.getString("roomStatus")));
                    rooms.add(room);
                }
            }
            log.info("Found {} rooms with type {} and status {}", rooms.size(), roomTypeId, roomStatus);
            return rooms;
        } catch (SQLException e) {
            log.error("Error finding rooms by status and type: ", e);
            throw new RuntimeException(e);
        }
    }

    public void updateRoomStatusBatch(List<Long> roomsIdx, RoomStatus roomStatus) {
        if (roomsIdx == null || roomsIdx.isEmpty()) {
            log.warn("No room IDs provided for batch update");
            return;
        }

        String sql = "UPDATE Room SET roomStatus = ? WHERE roomId = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (Long roomId : roomsIdx) {
                    preparedStatement.setString(1, roomStatus.name());
                    preparedStatement.setLong(2, roomId);
                    preparedStatement.addBatch();
                }

                int[] updateCounts = preparedStatement.executeBatch();
                connection.commit();

                log.info("Updated room status for {} rooms to {}", updateCounts.length, roomStatus.name());
            } catch (SQLException e) {
                connection.rollback();
                log.error("Error in batch update, rolled back transaction", e);
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            log.error("Error updating room statuses in batch: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Close the connection when repository is no longer needed
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                log.info("Database connection closed");
            }
        } catch (SQLException e) {
            log.error("Error closing database connection: ", e);
        }
    }
}
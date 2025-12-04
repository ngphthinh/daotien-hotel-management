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
import java.time.LocalDateTime;
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
        String sql = "INSERT INTO Room (roomNumber, roomTypeId, createdAt, roomStatus) VALUES (?, ?, ?, ?)";
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
        String sql = "UPDATE Room SET roomNumber = ?, roomTypeId = ?, roomStatus = ? WHERE roomId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getRoomNumber());
            preparedStatement.setString(2, entity.getRoomTypeId());
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
                        room.setRoomType(roomTypeRepository.findById(typeId));
                    }

                    room.setCreateAt(resultSet.getDate("createdAt") != null ? resultSet.getDate("createdAt").toLocalDate() : null);  // Null-safe
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
                room.setRoomStatus(RoomStatus.fromString(resultSet.getString("roomStatus")));
                rooms.add(room);
            }
            log.info("Found {} rooms in total", rooms.size());  // Log để debug

        } catch (SQLException e) {
            log.error("Error finding all Rooms: ", e);
            throw new RuntimeException(e);
        }

        return rooms;
    }


    public List<Room> findByRoomNumberOrId(String keyword) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE roomNumber LIKE ? OR roomId LIKE ? ORDER BY roomId ASC, roomNumber ASC";

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

                    room.setCreateAt(resultSet.getDate("createdAt") != null
                            ? resultSet.getDate("createdAt").toLocalDate()
                            : null);
                    rooms.add(room);
                }
            }
            log.info("Found {} rooms by keyword '{}'", rooms.size(), keyword);

        } catch (SQLException e) {
            AppLogger.error("Error finding rooms by keyword: ", e);
            throw new RuntimeException("Error finding rooms by name or ID", e);
        }
        return rooms;
    }

    public Room findRoomByRoomTypeAndStatusAndOverNights(String roomTypeId, RoomStatus roomStatus) {

        return null;
    }

    public List<Room> findRoomByStatusAndRoomType(String roomTypeId, RoomStatus roomStatus) {

        String sql = """
                select r.roomId, r.roomNumber, r.roomTypeId, r.roomStatus, rt.name
                from Room r join RoomType rt on r.roomTypeId = rt.roomTypeId
                where rt.roomTypeId = ? and r.roomStatus = ?
                order by r.roomId asc
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
                    room.setRoomStatus(RoomStatus.valueOf(resultSet.getString("roomStatus")));

                    RoomType roomType = new RoomType();
                    roomType.setRoomTypeId(resultSet.getString("roomTypeId"));
                    roomType.setName(resultSet.getString("name"));
                    room.setRoomType(roomType);

                    room.setRoomStatus(RoomStatus.fromString(resultSet.getString("roomStatus")));
                    rooms.add(room);
                }
            }
            return rooms;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateRoomStatusBatch(List<Long> roomsIdx, RoomStatus roomStatus) {
        String sql = "UPDATE Room SET roomStatus = ? WHERE roomId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Long roomId : roomsIdx) {
                preparedStatement.setString(1, roomStatus.name());
                preparedStatement.setLong(2, roomId);
                preparedStatement.addBatch();
            }
            int[] updateCounts = preparedStatement.executeBatch();
            log.info("Updated room status for {} rooms to {}", updateCounts.length, roomStatus.name());
        } catch (SQLException e) {
            log.error("Error updating room statuses in batch: ", e);
            throw new RuntimeException(e);
        }
    }

    public List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, RoomStatus roomStatus) {

        String sql = """
               
                SELECT r.*
                FROM Room r
                   WHERE r.roomStatus = ?
                  AND r.roomId NOT IN (
                      SELECT b.roomId
                      FROM Booking b
                      WHERE (b.checkInDate < ? AND b.checkOutDate > ?)
                         OR (b.checkInDate >= ? AND b.checkInDate < ?)
                         OR (b.checkOutDate > ? AND b.checkOutDate <= ?)
                  )
                ORDER BY r.roomId ASC
                """;

        List<Room> rooms = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roomStatus.name());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(checkOut));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(checkIn));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(checkIn));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(checkOut));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(checkIn));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(checkOut));

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

                    room.setCreateAt(resultSet.getDate("createdAt") != null
                            ? resultSet.getDate("createdAt").toLocalDate()
                            : null);
                    room.setRoomStatus(RoomStatus.fromString(resultSet.getString("roomStatus")));
                    rooms.add(room);
                }
            }
            log.info("Found {} available rooms between {} and {}", rooms.size(), checkIn, checkOut);

        } catch (SQLException e) {
            log.error("Error finding available Rooms: ", e);
            throw new RuntimeException(e);
        }
        return rooms;
    }
}
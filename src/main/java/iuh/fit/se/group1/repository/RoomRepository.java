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
        String sql = "INSERT INTO Room (roomNumber, roomTypeId, createdAt, roomStatus, isDeleted) VALUES (?, ?, ?, ?, ?)";
        try (
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            connection.setAutoCommit(true);

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
            ps.setBoolean(5, false);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating room failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    room.setRoomId(generatedKeys.getLong(1));
                }
            }

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
            preparedStatement.setString(3, entity.getRoomStatus().name());
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
        String sql = "SELECT * FROM Room WHERE roomId = ? AND isDeleted = 0";
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
        String sql = "UPDATE ROOM SET isDeleted = 1 where roomId = ?";
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

        String sql = """
                SELECT
                    r.roomId,
                    r.roomNumber,
                    r.createdAt AS roomCreatedAt,
                    r.roomStatus,
                    r.roomTypeId,
                
                    rt.name AS roomTypeName,
                    rt.hourlyRate,
                    rt.dailyRate,
                    rt.overnightRate,
                    rt.additionalHourRate,
                    rt.createdAt AS roomTypeCreatedAt
                FROM Room r
                JOIN RoomType rt ON r.roomTypeId = rt.roomTypeId
                WHERE r.isDeleted = 0
                ORDER BY r.roomId
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Room room = new Room();

                // ===== ROOM =====
                room.setRoomId(rs.getLong("roomId"));

                // ✅ FIX LỖI COMPILER Ở ĐÂY
                room.setRoomNumber(String.valueOf(rs.getInt("roomNumber")));

                room.setRoomStatus(RoomStatus.valueOf(rs.getString("roomStatus")));

                Date roomCreatedAt = rs.getDate("roomCreatedAt");
                room.setCreateAt(roomCreatedAt != null ? roomCreatedAt.toLocalDate() : null);

                // ===== ROOM TYPE =====
                RoomType roomType = new RoomType();
                roomType.setRoomTypeId(rs.getString("roomTypeId"));
                roomType.setName(rs.getString("roomTypeName"));
                roomType.setHourlyRate(rs.getBigDecimal("hourlyRate"));
                roomType.setDailyRate(rs.getBigDecimal("dailyRate"));
                roomType.setOvernightRate(rs.getBigDecimal("overnightRate"));
                roomType.setAdditionalHourRate(rs.getBigDecimal("additionalHourRate"));

                Date rtCreatedAt = rs.getDate("roomTypeCreatedAt");
                roomType.setCreatedAt(
                        rtCreatedAt != null ? rtCreatedAt.toLocalDate() : null
                );

                room.setRoomType(roomType);

                rooms.add(room);
            }

            log.info("Found {} rooms with RoomType", rooms.size());

        } catch (SQLException e) {
            log.error("Error loading rooms with RoomType", e);
            throw new RuntimeException(e);
        }

        return rooms;
    }

    public List<Room> findByRoomNumberOrId(String keyword) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE isDeleted = 0 and (roomNumber LIKE ? OR roomId LIKE ?) ORDER BY roomId ASC, roomNumber ASC";

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


    public List<Room> findRoomByStatusAndRoomType(String roomTypeId, RoomStatus roomStatus) {

        String sql = """
                select r.roomId, r.roomNumber, r.roomTypeId, r.roomStatus, rt.name
                from Room r join RoomType rt on r.roomTypeId = rt.roomTypeId
                where rt.roomTypeId = ? and r.roomStatus = ? and r.isDeleted = 0
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
                WHERE isDeleted = 0 and r.roomStatus = ?
                  AND r.roomId NOT IN (
                      SELECT b.roomId
                      FROM Booking b
                      WHERE b.checkInDate < ?   
                        AND b.checkOutDate > ?  
                  )
                ORDER BY r.roomId ASC;
                """;

        List<Room> rooms = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roomStatus.name());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(checkOut));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(checkIn));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Room room = new Room();
                    room.setRoomId(resultSet.getLong("roomId"));
                    room.setRoomNumber(resultSet.getString("roomNumber"));

                    String typeId = resultSet.getString("roomTypeId");
                    if (typeId != null) {
                        room.setRoomType(roomTypeRepository.findById(typeId));
                    }

                    room.setCreatedAt(resultSet.getDate("createdAt") != null
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

    public boolean existsByRoomNumber(String roomNumber) {
        String sql = "SELECT COUNT(*) FROM Room WHERE roomNumber = ? and isDeleted = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roomNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            log.error("Error checking existence of room number: ", e);
            throw new RuntimeException(e);
        }
        return false;
    }

    // ==================== Dashboard Methods ====================

    /**
     * Đếm tổng số phòng trong hệ thống
     */
    public int countTotalRooms() {
        String sql = "SELECT COUNT(*) FROM Room WHERE isDeleted = 0";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Error counting total rooms: ", e);
        }
        return 0;
    }

    /**
     * Đếm số phòng theo trạng thái
     */
    public int countByStatus(RoomStatus status) {
        String sql = "SELECT COUNT(*) FROM Room WHERE roomStatus = ? AND isDeleted = 0";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Error counting rooms by status {}: ", status, e);
        }
        return 0;
    }

    /**
     * Cập nhật phòng trong booking (dùng khi thay thế phòng)
     */
    public void updateBookingRoom(Long bookingId, Long newRoomId) {
        String sql = "UPDATE Booking SET roomId = ? WHERE bookingId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, newRoomId);
            ps.setLong(2, bookingId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                log.info("Updated booking {} to use room {}", bookingId, newRoomId);
            } else {
                log.warn("No booking found with ID: {}", bookingId);
            }
        } catch (SQLException e) {
            log.error("Error updating booking room: ", e);
            throw new RuntimeException("Error updating booking room", e);
        }
    }

    /**
     * Kiểm tra xem phòng có đang được sử dụng trong các order đang hoạt động không
     * @param roomId ID phòng cần kiểm tra
     * @return true nếu phòng đang được dùng, false nếu có thể xóa
     */
    public boolean isRoomInUse(Long roomId) {
        // Kiểm tra xem phòng có trong booking của các order đang xử lý không
        // Order type: 2 = Đang xử lý (không thể xóa phòng)
        String sql = """
            SELECT COUNT(*) 
            FROM Booking b
            JOIN Orders o ON b.orderId = o.orderId
            WHERE b.roomId = ? 
              AND o.orderTypeId = 2
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // true = đang được dùng, không thể xóa
                }
            }
        } catch (SQLException e) {
            log.error("Error checking if room is in use: ", e);
            throw new RuntimeException("Error checking room usage", e);
        }
        return false; // Không tìm thấy -> có thể xóa
    }
}
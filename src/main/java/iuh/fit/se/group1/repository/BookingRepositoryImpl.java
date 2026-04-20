package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import iuh.fit.se.group1.repository.interfaces.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookingRepositoryImpl implements Repository<Booking, Long>, iuh.fit.se.group1.repository.interfaces.BookingRepository {

    private static final Logger log = LoggerFactory.getLogger(BookingRepositoryImpl.class);
    private final Connection connection;

    public BookingRepositoryImpl() {
        this.connection = DatabaseUtil.getConnection();
    }

    public boolean isExistsByRoomAndDate(Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        String sql = "SELECT COUNT(*) FROM Booking WHERE roomId = ? AND (checkInDate < ? AND checkOutDate > ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, roomId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(checkOutDate));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(checkInDate));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    @Override
    public Booking save(Booking entity) {
        String sql = """
            INSERT INTO Booking(checkInDate, checkOutDate, orderId, roomId, bookingType, createdAt)
            values (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(entity.getCheckInDate()));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(entity.getCheckOutDate()));
            preparedStatement.setLong(3, entity.getOrder().getOrderId());
            preparedStatement.setLong(4, entity.getRoom().getRoomId());
            preparedStatement.setString(5, entity.getBookingType().toString());
            entity.setCreatedAt(LocalDate.now());
            preparedStatement.setDate(6, java.sql.Date.valueOf(entity.getCreatedAt()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setBookingId(generatedKeys.getLong(1));
                }
            }

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Booking findById(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public List<Booking> findAll() {
        return null;
    }

    @Override
    public Booking update(Booking entity) {
        return null;
    }

    @Override
    public void saveAllBookingsForOrder(Order savedOrder, List<Booking> bookings) {
        for (Booking booking : bookings) {
            booking.setOrder(savedOrder);
            this.save(booking);
        }
    }

    @Override
    public void removeBookingsFromOrder(Order currentOrder, List<Booking> result) {
        String sql = "DELETE FROM Booking WHERE orderId = ? AND bookingId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Booking booking : currentOrder.getBookings()) {
                if (!result.contains(booking)) {
                    preparedStatement.setLong(1, currentOrder.getOrderId());
                    preparedStatement.setLong(2, booking.getBookingId());
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Move a set of existing booking records to another order by updating their orderId.
     * This performs a batch UPDATE Booking SET orderId = ? WHERE bookingId = ?
     */
    @Override
    public void moveBookingsToOrder(Long targetOrderId, List<Long> bookingIds) {
        if (bookingIds == null || bookingIds.isEmpty()) return;
        String sql = "UPDATE Booking SET orderId = ? WHERE bookingId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Long bookingId : bookingIds) {
                ps.setLong(1, targetOrderId);
                ps.setLong(2, bookingId);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update check-in and check-out timestamps for a specific booking
     */
    @Override
    public void updateBookingDates(Long bookingId, LocalDateTime checkIn, LocalDateTime checkOut) {
        String sql = "UPDATE Booking SET checkInDate = ?, checkOutDate = ? WHERE bookingId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(checkIn));
            ps.setTimestamp(2, Timestamp.valueOf(checkOut));
            ps.setLong(3, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating booking dates", e);
        }
    }


    @Override
    public void deleteByOrderId(Long id) {
        String sql = "DELETE FROM Booking WHERE orderId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> findByOrderId(Long orderId) {
        String sql = """
                SELECT bookingId, orderId, checkInDate, checkOutDate, bookingType, R.roomId, roomNumber, roomStatus, RT.roomTypeId, name, hourlyRate, dailyRate, overnightRate, additionalHourRate
                FROM Booking B join Room R on B.roomId = R.roomId join RoomType RT on RT.roomTypeId = R.roomTypeId
                WHERE B.orderId = ?
                    """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Booking> bookings = new java.util.ArrayList<>();
                while (resultSet.next()) {

                    Booking booking = new Booking();
                    booking.setBookingId(resultSet.getLong("bookingId"));
                    booking.setCheckInDate(resultSet.getTimestamp("checkInDate").toLocalDateTime());
                    booking.setCheckOutDate(resultSet.getTimestamp("checkOutDate").toLocalDateTime());
                    booking.setBookingType(BookingType.valueOf(resultSet.getString("bookingType")));

                    Room room = new Room();
                    room.setRoomId(resultSet.getLong("roomId"));
                    room.setRoomNumber(resultSet.getString("roomNumber"));
                    // Set RoomType
                    RoomType roomType = new RoomType();
                    roomType.setRoomTypeId(resultSet.getString("roomTypeId"));
                    roomType.setName(resultSet.getString("name"));
                    roomType.setHourlyRate(resultSet.getBigDecimal("hourlyRate"));
                    roomType.setDailyRate(resultSet.getBigDecimal("dailyRate"));
                    roomType.setOvernightRate(resultSet.getBigDecimal("overnightRate"));
                    roomType.setAdditionalHourRate(resultSet.getBigDecimal("additionalHourRate"));
                    room.setRoomType(roomType);
                    booking.setRoom(room);

                    bookings.add(booking);
                }
                return bookings;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Đếm số phòng sắp hết hạn (checkout trong khoảng thời gian)
     */
    @Override
    public int countRoomsNearExpiry(LocalDateTime fromTime, LocalDateTime toTime) {
        String sql = "SELECT COUNT(DISTINCT b.roomId) " +
                     "FROM Booking b " +
                     "WHERE b.checkOutDate BETWEEN ? AND ? " +
                     "AND b.checkOutDate IS NOT NULL";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromTime));
            ps.setTimestamp(2, Timestamp.valueOf(toTime));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Error counting rooms near expiry: ", e);
        }
        return 0;
    }

    /**
     * Đếm số lượt check-in trong khoảng thời gian
     */
    @Override
    public int countCheckIns(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COUNT(*) FROM Booking " +
                     "WHERE checkInDate BETWEEN ? AND ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Error counting check-ins: ", e);
        }
        return 0;
    }

    /**
     * Đếm số lượt check-out trong khoảng thời gian
     */
    @Override
    public int countCheckOuts(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COUNT(*) FROM Booking " +
                     "WHERE checkOutDate BETWEEN ? AND ? " +
                     "AND checkOutDate IS NOT NULL";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Error counting check-outs: ", e);
        }
        return 0;
    }

    /**
     * Đếm phòng đã checkout trong khoảng thời gian
     */
    @Override
    public int countCheckedOutRooms(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COUNT(*) FROM Booking " +
                     "WHERE checkOutDate BETWEEN ? AND ? " +
                     "AND checkOutDate IS NOT NULL";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Error counting checked out rooms: ", e);
        }
        return 0;
    }

    /**
     * Đếm phòng trả trễ (checkout quá giờ quy định)
     */
    @Override
    public int countLateCheckOuts(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime deadlineTime) {
        String sql = "SELECT COUNT(*) FROM Booking " +
                     "WHERE checkOutDate BETWEEN ? AND ? " +
                     "AND checkOutDate < ? " +
                     "AND checkOutDate IS NOT NULL";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            ps.setTimestamp(3, Timestamp.valueOf(deadlineTime));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Error counting late checkouts: ", e);
        }
        return 0;
    }
}

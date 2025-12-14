package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookingRepository implements Repository<Booking, Long>{

    private static final Logger log = LoggerFactory.getLogger(BookingRepository.class);
    private final Connection connection;

    public BookingRepository() {
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

    public void saveAllBookingsForOrder(Order savedOrder, List<Booking> bookings) {
        for (Booking booking : bookings) {
            booking.setOrder(savedOrder);
            this.save(booking);
        }
    }

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
}

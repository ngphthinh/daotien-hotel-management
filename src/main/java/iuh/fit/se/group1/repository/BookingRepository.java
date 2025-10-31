package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookingRepository implements Repository<Booking, Long>{

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
            INSERT INTO Booking(checkInDate, checkOutDate, employeeId, orderId, roomId, bookingType, totalPrice, createdAt)
            values (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(entity.getCheckInDate()));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(entity.getCheckOutDate()));
            preparedStatement.setLong(3, entity.getEmployee().getEmployeeId());
            preparedStatement.setLong(4, entity.getOrder().getOrderId());
            preparedStatement.setLong(5, entity.getRoom().getRoomId());
            preparedStatement.setString(6, entity.getBookingType().toString());
            preparedStatement.setBigDecimal(7, entity.getTotalPrice());
            entity.setCreatedAt(LocalDate.now());
            preparedStatement.setDate(8, java.sql.Date.valueOf(entity.getCreatedAt()));

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
}

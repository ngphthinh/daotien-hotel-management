package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.dto.BookingDTO;
import iuh.fit.se.group1.dto.BookingDisplayDTO;
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
import java.util.ArrayList;
import java.util.List;

public class BookingRepositoryImpl implements Repository<Booking, Long>, iuh.fit.se.group1.repository.interfaces.BookingRepository {

    /**
     * Gia hạn ngày check-out cho booking
     */
    @Override
    public boolean extendRoomBooking(Long orderId, List<Long> roomIds,
                                     int extendValue, String bookingType) {
        try {
            connection.setAutoCommit(false);

            // Validate booking type
            if ("OVERNIGHT".equals(bookingType)) {
                log.warn("Cannot extend OVERNIGHT booking type");
                connection.rollback();
                return false;
            }

            // Lấy thông tin booking hiện tại
            String getBookingSql = """
                    SELECT checkInDate, checkOutDate
                    FROM Booking
                    WHERE orderId = ? AND bookingType = ? AND roomId = ?
                    """;

            LocalDateTime currentCheckOut = null;

            try (PreparedStatement stmt = connection.prepareStatement(getBookingSql)) {
                stmt.setLong(1, orderId);
                stmt.setString(2, bookingType);
                stmt.setLong(3, roomIds.get(0)); // Lấy từ phòng đầu tiên

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Timestamp checkOutTimestamp = rs.getTimestamp("checkOutDate");
                    if (checkOutTimestamp != null) {
                        currentCheckOut = checkOutTimestamp.toLocalDateTime();
                    }
                } else {
                    connection.rollback();
                    log.warn("Booking not found for orderId: {}", orderId);
                    return false;
                }
            }

            if (currentCheckOut == null) {
                connection.rollback();
                return false;
            }

            // Tính checkout date mới dựa trên booking type
            LocalDateTime newCheckOut;
            if ("HOURLY".equals(bookingType)) {
                // Gia hạn theo giờ
                newCheckOut = currentCheckOut.plusHours(extendValue);
                log.info("Extending HOURLY booking by {} hours. Current: {}, New: {}",
                        extendValue, currentCheckOut, newCheckOut);
            } else if ("DAILY".equals(bookingType)) {
                // Gia hạn theo ngày
                newCheckOut = currentCheckOut.plusDays(extendValue);
                log.info("Extending DAILY booking by {} days. Current: {}, New: {}",
                        extendValue, currentCheckOut, newCheckOut);
            } else {
                connection.rollback();
                log.warn("Invalid booking type for extension: {}", bookingType);
                return false;
            }

            // Cập nhật checkout date cho tất cả phòng
            String updateSql = """
                    UPDATE Booking
                    SET checkOutDate = ?
                    WHERE orderId = ? AND bookingType = ? AND roomId = ?
                    """;

            try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                for (Long roomId : roomIds) {
                    stmt.setTimestamp(1, Timestamp.valueOf(newCheckOut));
                    stmt.setLong(2, orderId);
                    stmt.setString(3, bookingType);
                    stmt.setLong(4, roomId);

                    int updated = stmt.executeUpdate();
                    if (updated == 0) {
                        log.warn("No booking updated for roomId: {}", roomId);
                    }
                }
            }

            connection.commit();
            log.info("Successfully extended {} bookings for order {}", roomIds.size(), orderId);
            return true;

        } catch (SQLException e) {
            log.error("Error extending room booking", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.error("Error rolling back transaction", ex);
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("Error resetting auto-commit", e);
            }
        }
    }

    @Override
    public List<BookingDisplayDTO> findAllBookingDisplay() {
        String sql = """
                SELECT
                    B.bookingId,
                    R.roomNumber,
                    C.fullName AS customerName,
                    C.phone
                FROM Booking B
                JOIN Room R ON B.roomId = R.roomId
                JOIN Orders O ON B.orderId = O.orderId
                JOIN Customer C ON O.customerId = C.customerId
                WHERE orderTypeId = 2
                """;

        List<BookingDisplayDTO> list = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Long bookingId = rs.getLong("bookingId");
                String roomNumber = rs.getString("roomNumber");
                String customerName = rs.getString("customerName");
                String phoneNumber = rs.getString("phone");
                BookingDisplayDTO dto = new BookingDisplayDTO(bookingId, roomNumber, customerName, phoneNumber);
                list.add(dto);
            }

        } catch (SQLException e) {
            log.error("Error retrieving booking display list: ", e);
            throw new RuntimeException(e);
        }

        return list;
    }

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


    @Override
    public List<BookingDTO> getAllBookings() {
        List<BookingDTO> bookings = new ArrayList<>();
        String sql = """
                    SELECT
                        MIN(b.bookingId) as bookingId,
                        c.fullName,
                        c.citizenId,
                        b.bookingType,
                        MIN(o.orderId) as orderId,
                        MIN(ot.name) as orderTypeName,
                        STRING_AGG(r.roomNumber, ', ') WITHIN GROUP (ORDER BY r.roomNumber) as rooms
                    FROM Booking b
                    JOIN Orders o ON b.orderId = o.orderId
                    JOIN Customer c ON o.customerId = c.customerId
                    JOIN Room r ON b.roomId = r.roomId
                    JOIN OrderType ot ON o.orderTypeId = ot.orderTypeId
                    WHERE o.orderTypeId IN (2, 3)
                    AND (
                        -- CASE 1: Đã check-in và chưa check-out (cho tất cả loại thuê)
                        (b.checkInDate <= GETDATE() AND b.checkOutDate >= GETDATE())
                        OR
                        -- CASE 2: Booking trong ngày hôm nay nhưng chưa check-in
                        (CAST(b.checkInDate AS DATE) = CAST(GETDATE() AS DATE) AND b.checkInDate > GETDATE())
                    )
                    GROUP BY c.citizenId, c.fullName, b.bookingType, o.orderId
                    ORDER BY c.citizenId, b.bookingType
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                try {
                    BookingDTO dto = new BookingDTO();
                    dto.bookingId = rs.getLong("bookingId");
                    dto.bookingIdDisplay = String.format("%d", rs.getLong("bookingId"));
                    dto.guestName = rs.getString("fullName");
                    dto.cccd = rs.getString("citizenId");

                    String bookingTypeStr = rs.getString("bookingType");
                    log.info("Found booking with type: {}", bookingTypeStr);

                    dto.bookingType = BookingType.valueOf(bookingTypeStr);
                    dto.rooms = rs.getString("rooms");
                    dto.orderId = rs.getLong("orderId");
                    dto.orderTypeName = rs.getString("orderTypeName");
                    bookings.add(dto);
                } catch (IllegalArgumentException e) {
                    log.error("Invalid booking type: {}", rs.getString("bookingType"), e);
                }
            }

        } catch (SQLException e) {
            log.error("Error getting bookings", e);
        }

        log.info("Total bookings found: {}", bookings.size());
        return bookings;
    }


    @Override
    public List<BookingDTO> searchBookingsByCitizenId(String citizenId) {
        List<BookingDTO> bookings = new ArrayList<>();
        String sql = """
                    SELECT
                        MIN(b.bookingId) as bookingId,
                        c.fullName,
                        c.citizenId,
                        b.bookingType,
                        MIN(o.orderId) as orderId,
                        MIN(ot.name) as orderTypeName,
                        STRING_AGG(r.roomNumber, ', ') WITHIN GROUP (ORDER BY r.roomNumber) as rooms
                    FROM Booking b
                    JOIN Orders o ON b.orderId = o.orderId
                    JOIN Customer c ON o.customerId = c.customerId
                    JOIN Room r ON b.roomId = r.roomId
                    JOIN OrderType ot ON o.orderTypeId = ot.orderTypeId
                    WHERE o.orderTypeId IN (2, 3)
                    AND (
                        -- CASE 1: Đã check-in và chưa check-out (cho tất cả loại thuê)
                        (b.checkInDate <= GETDATE() AND b.checkOutDate >= GETDATE())
                        OR
                        -- CASE 2: Booking trong ngày hôm nay nhưng chưa check-in
                        (CAST(b.checkInDate AS DATE) = CAST(GETDATE() AS DATE) AND b.checkInDate > GETDATE())
                    )
                    AND c.citizenId LIKE ?
                    GROUP BY c.citizenId, c.fullName, b.bookingType, o.orderId
                    ORDER BY c.citizenId, b.bookingType
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + citizenId + "%";
            stmt.setString(1, searchPattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                try {
                    BookingDTO dto = new BookingDTO();
                    dto.bookingId = rs.getLong("bookingId");
                    dto.bookingIdDisplay = String.format("%d", rs.getLong("bookingId"));
                    dto.guestName = rs.getString("fullName");
                    dto.cccd = rs.getString("citizenId");

                    String bookingTypeStr = rs.getString("bookingType");
                    dto.bookingType = BookingType.valueOf(bookingTypeStr);
                    dto.rooms = rs.getString("rooms");
                    dto.orderId = rs.getLong("orderId");
                    dto.orderTypeName = rs.getString("orderTypeName");
                    bookings.add(dto);
                } catch (IllegalArgumentException e) {
                    log.error("Invalid booking type: {}", rs.getString("bookingType"), e);
                }
            }

        } catch (SQLException e) {
            log.error("Error searching bookings by citizen ID", e);
        }

        return bookings;
    }


    /**
     * Lấy thông tin booking chi tiết
     */
    @Override
    public Booking getBookingById(long bookingId, long roomId) {
        String sql = """
                SELECT checkInDate, checkOutDate, bookingType, orderId
                FROM Booking
                WHERE bookingId = ? AND roomId = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, bookingId);
            stmt.setLong(2, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(bookingId);
                booking.setCheckInDate(rs.getTimestamp("checkInDate").toLocalDateTime());
                booking.setCheckOutDate(rs.getTimestamp("checkOutDate").toLocalDateTime());
                booking.setBookingType(BookingType.valueOf(rs.getString("bookingType")));

                Order order = new Order();
                order.setOrderId(rs.getLong("orderId"));
                booking.setOrder(order);

                return booking;
            }

        } catch (SQLException e) {
            log.error("Error getting booking by ID", e);
        }

        return null;
    }


    /**
     * Lấy thông tin booking chi tiết theo orderId và bookingType
     */
    @Override
    public Booking getBookingByOrderIdAndType(long orderId, String bookingType, long roomId) {
        String sql = """
                SELECT checkInDate, checkOutDate, bookingType, orderId, bookingId
                FROM Booking
                WHERE orderId = ? AND bookingType = ? AND roomId = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            stmt.setString(2, bookingType);
            stmt.setLong(3, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getLong("bookingId"));
                booking.setCheckInDate(rs.getTimestamp("checkInDate").toLocalDateTime());
                booking.setCheckOutDate(rs.getTimestamp("checkOutDate").toLocalDateTime());
                booking.setBookingType(BookingType.valueOf(rs.getString("bookingType")));

                Order order = new Order();
                order.setOrderId(rs.getLong("orderId"));
                booking.setOrder(order);

                return booking;
            }

        } catch (SQLException e) {
            log.error("Error getting booking by orderId and type", e);
        }

        return null;
    }

    /**
     * Implement cancelRoomBooking method to cancel room bookings
     * Xóa booking và cập nhật trạng thái phòng về AVAILABLE
     */
    @Override
    public boolean cancelRoomBooking(Long orderId, Long roomId, String bookingType) {
        try {
            connection.setAutoCommit(false);

            // 1. Check booking tồn tại và chưa check-in
            String checkBookingSql = """
                    SELECT checkInDate
                    FROM Booking
                    WHERE orderId = ? AND bookingType = ? AND roomId = ?
                    """;

            Timestamp checkInTimestamp = null;

            try (PreparedStatement stmt = connection.prepareStatement(checkBookingSql)) {
                stmt.setLong(1, orderId);
                stmt.setString(2, bookingType);
                stmt.setLong(3, roomId);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    checkInTimestamp = rs.getTimestamp("checkInDate");
                } else {
                    connection.rollback();
                    log.warn("Booking not found for orderId: {}, roomId: {}", orderId, roomId);
                    return false;
                }
            }

            // Nếu đã check-in → không cho hủy
            if (checkInTimestamp != null &&
                    checkInTimestamp.before(Timestamp.from(java.time.Instant.now()))) {
                connection.rollback();
                log.warn("Cannot cancel room booking: already checked in.");
                return false;
            }

            // 2. Xóa booking
            String deleteBookingSql = """
                    DELETE FROM Booking
                    WHERE orderId = ? AND bookingType = ? AND roomId = ?
                    """;

            try (PreparedStatement stmt = connection.prepareStatement(deleteBookingSql)) {
                stmt.setLong(1, orderId);
                stmt.setString(2, bookingType);
                stmt.setLong(3, roomId);
                stmt.executeUpdate();
            }

//            // 3. Update room status → AVAILABLE
//            String updateRoomSql = "UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = ?";
//            try (PreparedStatement stmt = connection.prepareStatement(updateRoomSql)) {
//                stmt.setLong(1, roomId);
//                stmt.executeUpdate();
//            }

            connection.commit();
            log.info("Cancelled booking for orderId {}, roomId {}", orderId, roomId);
            return true;

        } catch (SQLException e) {
            log.error("Error canceling room booking", e);
            try {
                connection.rollback();
            } catch (Exception ignored) {
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception ignored) {
            }
        }
    }
}

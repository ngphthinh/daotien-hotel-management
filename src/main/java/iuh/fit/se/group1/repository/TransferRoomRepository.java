package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransferRoomRepository {
    private static final Logger log = LoggerFactory.getLogger(TransferRoomRepository.class);
    private final Connection connection;

    public TransferRoomRepository() {
        connection = DatabaseUtil.getConnection();
    }

    /**
     * Lấy tất cả bookings - nhóm theo CCCD + bookingType (mỗi CCCD có thể có nhiều
     * dòng nếu khác loại thuê)
     */
    
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
     * Lấy thông tin TẤT CẢ phòng theo orderId và bookingType
     * (vì giờ 1 CCCD có nhiều phòng được gộp lại, cần lấy theo orderId)
     */
    public List<Room> getRoomsByOrderIdAndType(long orderId, String bookingType) {
        List<Room> rooms = new ArrayList<>();
        String sql = """
                    SELECT
                        r.roomId,
                        r.roomNumber,
                        r.roomStatus,
                        rt.roomTypeId,
                        rt.name as roomTypeName,
                        rt.hourlyRate,
                        rt.dailyRate,
                        rt.overnightRate,
                        rt.additionalHourRate
                    FROM Booking b
                    JOIN Room r ON b.roomId = r.roomId
                    JOIN RoomType rt ON r.roomTypeId = rt.roomTypeId
                    WHERE b.orderId = ? AND b.bookingType = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            stmt.setString(2, bookingType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RoomType roomType = new RoomType();
                roomType.setRoomTypeId(rs.getString("roomTypeId"));
                roomType.setName(rs.getString("roomTypeName"));
                roomType.setHourlyRate(rs.getBigDecimal("hourlyRate"));
                roomType.setDailyRate(rs.getBigDecimal("dailyRate"));
                roomType.setOvernightRate(rs.getBigDecimal("overnightRate"));
                roomType.setAdditionalHourRate(rs.getBigDecimal("additionalHourRate"));

                Room room = new Room();
                room.setRoomId(rs.getLong("roomId"));
                room.setRoomNumber(rs.getString("roomNumber"));
                room.setRoomType(roomType);

                rooms.add(room);
            }

        } catch (SQLException e) {
            log.error("Error getting rooms by orderId and bookingType", e);
        }

        return rooms;
    }

    /**
     * Lấy thông tin phòng theo booking ID (giữ lại cho tương thích)
     */
    public List<Room> getRoomsByBookingId(long bookingId) {
        List<Room> rooms = new ArrayList<>();
        String sql = """
                    SELECT
                        r.roomId,
                        r.roomNumber,
                        r.roomStatus,
                        rt.roomTypeId,
                        rt.name as roomTypeName,
                        rt.hourlyRate,
                        rt.dailyRate,
                        rt.overnightRate,
                        rt.additionalHourRate
                    FROM Booking b
                    JOIN Room r ON b.roomId = r.roomId
                    JOIN RoomType rt ON r.roomTypeId = rt.roomTypeId
                    WHERE b.bookingId = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RoomType roomType = new RoomType();
                roomType.setRoomTypeId(rs.getString("roomTypeId"));
                roomType.setName(rs.getString("roomTypeName"));
                roomType.setHourlyRate(rs.getBigDecimal("hourlyRate"));
                roomType.setDailyRate(rs.getBigDecimal("dailyRate"));
                roomType.setOvernightRate(rs.getBigDecimal("overnightRate"));
                roomType.setAdditionalHourRate(rs.getBigDecimal("additionalHourRate"));

                Room room = new Room();
                room.setRoomId(rs.getLong("roomId"));
                room.setRoomNumber(rs.getString("roomNumber"));
                room.setRoomType(roomType);

                rooms.add(room);
            }

        } catch (SQLException e) {
            log.error("Error getting rooms by booking ID", e);
        }

        return rooms;
    }

    /**
     * Lấy danh sách phòng trống theo loại phòng
     */
    public List<Room> getAvailableRoomsByType(String roomTypeId) {
        List<Room> rooms = new ArrayList<>();
        String sql = """
                SELECT
                    r.roomId,
                    r.roomNumber,
                    r.roomStatus,
                    rt.roomTypeId,
                    rt.name as roomTypeName,
                    rt.hourlyRate,
                    rt.dailyRate,
                    rt.overnightRate,
                    rt.additionalHourRate
                FROM Room r
                JOIN RoomType rt ON r.roomTypeId = rt.roomTypeId
                WHERE r.roomStatus = 'AVAILABLE'
                AND r.roomTypeId = ?
                ORDER BY r.roomNumber
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomTypeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RoomType roomType = new RoomType();
                roomType.setRoomTypeId(rs.getString("roomTypeId"));
                roomType.setName(rs.getString("roomTypeName"));
                roomType.setHourlyRate(rs.getBigDecimal("hourlyRate"));
                roomType.setDailyRate(rs.getBigDecimal("dailyRate"));
                roomType.setOvernightRate(rs.getBigDecimal("overnightRate"));
                roomType.setAdditionalHourRate(rs.getBigDecimal("additionalHourRate"));

                Room room = new Room();
                room.setRoomId(rs.getLong("roomId"));
                room.setRoomNumber(rs.getString("roomNumber"));
                room.setRoomType(roomType);

                rooms.add(room);
            }

        } catch (SQLException e) {
            log.error("Error getting available rooms", e);
        }

        return rooms;
    }

    /**
     * Lấy thông tin booking chi tiết
     */
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
     * Thực hiện chuyển phòng theo orderId và bookingType (cho phép 1 phòng -> nhiều
     * phòng)
     */
    public boolean transferRooms(long orderId, String bookingType, List<Long> oldRoomIds, List<Long> newRoomIds) {
        try {
            connection.setAutoCommit(false);

            // 1. Lấy thông tin booking gốc từ orderId, bookingType và phòng đầu tiên
            Booking bookingInfo = getBookingByOrderIdAndType(orderId, bookingType, oldRoomIds.get(0));
            if (bookingInfo == null) {
                connection.rollback();
                return false;
            }

            // 2. Cập nhật trạng thái phòng cũ về AVAILABLE
            String updateOldRooms = "UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateOldRooms)) {
                for (Long roomId : oldRoomIds) {
                    stmt.setLong(1, roomId);
                    stmt.executeUpdate();
                }
            }

            // 3. Xóa booking cũ theo orderId và bookingType
            String deleteOldBookings = "DELETE FROM Booking WHERE orderId = ? AND bookingType = ? AND roomId = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteOldBookings)) {
                for (Long roomId : oldRoomIds) {
                    stmt.setLong(1, orderId);
                    stmt.setString(2, bookingType);
                    stmt.setLong(3, roomId);
                    stmt.executeUpdate();
                }
            }

            // 4. Tạo booking mới với các phòng mới
            String insertNewBookings = """
                        INSERT INTO Booking (orderId, roomId, checkInDate, checkOutDate, bookingType, createdAt)
                        VALUES (?, ?, ?, ?, ?, GETDATE())
                    """;

            try (PreparedStatement stmt = connection.prepareStatement(insertNewBookings)) {
                for (Long roomId : newRoomIds) {
                    stmt.setLong(1, orderId);
                    stmt.setLong(2, roomId);
                    stmt.setTimestamp(3, Timestamp.valueOf(bookingInfo.getCheckInDate()));
                    stmt.setTimestamp(4, Timestamp.valueOf(bookingInfo.getCheckOutDate()));
                    stmt.setString(5, bookingType);
                    stmt.executeUpdate();
                }
            }

            // 5. Cập nhật trạng thái phòng mới
            String updateNewRooms = "UPDATE Room SET roomStatus = 'OCCUPIED' WHERE roomId = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateNewRooms)) {
                for (Long roomId : newRoomIds) {
                    stmt.setLong(1, roomId);
                    stmt.executeUpdate();
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            log.error("Error transferring rooms", e);
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

    /**
     * Lấy thông tin booking chi tiết theo orderId và bookingType
     */
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
     * Thêm surcharge vào order
     */
    public boolean addSurchargeToOrder(long orderId, long surchargeAmount) {
        if (surchargeAmount == 0)
            return true;

        String sql = "UPDATE Orders SET totalAmount = totalAmount + ? WHERE orderId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, surchargeAmount);
            stmt.setLong(2, orderId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            log.error("Error adding surcharge to order", e);
            return false;
        }
    }

    /**
     * DTO class cho transfer data
     */
    public static class BookingDTO {
        public long bookingId;
        public String bookingIdDisplay;
        public String guestName;
        public String cccd;
        public BookingType bookingType;
        public String rooms;
        public long orderId;
        public String orderTypeName;
    }

    /**
     * Implement cancelRoomBooking method to cancel room bookings
     * Xóa booking và cập nhật trạng thái phòng về AVAILABLE
     */
    public boolean cancelRoomBooking(Long orderId, String roomNumber, String bookingType) {
    try {
        connection.setAutoCommit(false);

        // 1. Tìm phòng theo roomNumber
        String findRoomSql = "SELECT roomId FROM Room WHERE roomNumber = ?";
        long roomId = -1;
        
        try (PreparedStatement stmt = connection.prepareStatement(findRoomSql)) {
            stmt.setString(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                roomId = rs.getLong("roomId");
            } else {
                connection.rollback();
                log.warn("Room not found: {}", roomNumber);
                return false;
            }
        }

        // 2. Kiểm tra trạng thái booking: chỉ cho phép hủy nếu là "đặt trước" (chưa check-in)
        String checkBookingSql = "SELECT checkInDate FROM Booking WHERE orderId = ? AND bookingType = ? AND roomId = ?";
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
                log.warn("Booking not found for orderId: {}, roomNumber: {}", orderId, roomNumber);
                return false;
            }
        }
        
        // Kiểm tra nếu đã check-in (checkInDate <= GETDATE()), thì không cho hủy
        if (checkInTimestamp != null && checkInTimestamp.before(Timestamp.from(java.time.Instant.now()))) {
            connection.rollback();
            log.warn("Cannot cancel room booking: Booking for orderId {} and room {} has already been checked in.", 
                    orderId, roomNumber);
            return false;
        }

        // 3. Xóa booking với orderId, bookingType và roomId
        String deleteBookingSql = "DELETE FROM Booking WHERE orderId = ? AND bookingType = ? AND roomId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteBookingSql)) {
            stmt.setLong(1, orderId);
            stmt.setString(2, bookingType);
            stmt.setLong(3, roomId);
            stmt.executeUpdate();
        }

        // 4. Cập nhật trạng thái phòng về AVAILABLE
        String updateRoomSql = "UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateRoomSql)) {
            stmt.setLong(1, roomId);
            stmt.executeUpdate();
        }

        connection.commit();
        log.info("Successfully cancelled room booking for orderId: {}, roomNumber: {}", orderId, roomNumber);
        return true;

    } catch (SQLException e) {
        log.error("Error canceling room booking", e);
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
/**
     * Implement extendRoomBooking method to extend room bookings
     * Gia hạn ngày check-out cho booking
     */
    public boolean extendRoomBooking(Long orderId, List<Long> roomIds, int days, String bookingType) {
        try {
            connection.setAutoCommit(false);

            // 1. Tính ngày check-out mới (thêm số ngày)
            String getCheckOutSql = """
                    SELECT MAX(checkOutDate) as checkOutDate
                    FROM Booking
                    WHERE orderId = ? AND bookingType = ? AND roomId = ?
                    """;

            LocalDateTime newCheckOutDate = null;

            // Lấy ngày check-out hiện tại từ booking đầu tiên
            try (PreparedStatement stmt = connection.prepareStatement(getCheckOutSql)) {
                stmt.setLong(1, orderId);
                stmt.setString(2, bookingType);
                stmt.setLong(3, roomIds.get(0));
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    Timestamp checkOutTimestamp = rs.getTimestamp("checkOutDate");
                    if (checkOutTimestamp != null) {
                        LocalDateTime currentCheckOut = checkOutTimestamp.toLocalDateTime();
                        // Thêm số ngày vào ngày check-out hiện tại
                        newCheckOutDate = currentCheckOut.plusDays(days);
                    }
                } else {
                    connection.rollback();
                    log.warn("Booking not found for extension");
                    return false;
                }
            }

            if (newCheckOutDate == null) {
                connection.rollback();
                return false;
            }

            // 2. Cập nhật checkOutDate cho tất cả phòng
            String updateCheckOutSql = """
                    UPDATE Booking
                    SET checkOutDate = ?
                    WHERE orderId = ? AND bookingType = ? AND roomId = ?
                    """;

            try (PreparedStatement stmt = connection.prepareStatement(updateCheckOutSql)) {
                for (Long roomId : roomIds) {
                    stmt.setTimestamp(1, Timestamp.valueOf(newCheckOutDate));
                    stmt.setLong(2, orderId);
                    stmt.setString(3, bookingType);
                    stmt.setLong(4, roomId);
                    stmt.executeUpdate();
                }
            }

            connection.commit();
            log.info("Successfully extended room booking for orderId: {}, new checkout: {}", orderId, newCheckOutDate);
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

}
package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.enums.TimeType;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service xử lý logic dashboard nhân viên
 */
public class DashboardService {

    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);
    private final Connection connection;

    public DashboardService() {
        this.connection = DatabaseUtil.getConnection();
    }

    /**
     * Lấy dữ liệu dashboard theo loại thời gian
     */
    public DashboardSummaryDto getDashboardData(TimeType timeType) {
        LocalDateTime startDate;
        LocalDateTime endDate = LocalDateTime.now();

        switch (timeType) {
            case TODAY:
                startDate = LocalDate.now().atStartOfDay();
                break;
            case DAYS_7:
                startDate = LocalDate.now().minusDays(7).atStartOfDay();
                break;
            case DAYS_30:
                startDate = LocalDate.now().minusDays(30).atStartOfDay();
                break;
            case DAYS_90:
                startDate = LocalDate.now().minusDays(90).atStartOfDay();
                break;
            default:
                startDate = LocalDate.now().atStartOfDay();
        }

        log.info("Fetching dashboard data from {} to {}", startDate, endDate);
        return getDashboardSummary(startDate, endDate);
    }

    /**
     * Lấy dữ liệu dashboard với khoảng thời gian tùy chỉnh
     */
    public DashboardSummaryDto getDashboardData(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        log.info("Fetching dashboard data from {} to {}", start, end);
        return getDashboardSummary(start, end);
    }

    /**
     * Lấy nguồn doanh thu theo loại: Dịch vụ, Phụ phí, Tiền phòng, Khác
     */
    public List<RevenueSourceDto> getRevenueSources(LocalDateTime startDate, LocalDateTime endDate) {
        List<RevenueSourceDto> results = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;

        // 1. Doanh thu từ DỊCH VỤ (OrderDetail - Amenity)
        BigDecimal serviceRevenue = BigDecimal.ZERO;
        String sqlService = "SELECT ISNULL(SUM(od.unitPrice * od.quantity), 0) as total " +
                           "FROM OrderDetail od " +
                           "JOIN Orders o ON od.orderId = o.orderId " +
                           "WHERE o.paymentDate IS NOT NULL AND CAST(o.paymentDate AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";

        try (PreparedStatement ps = connection.prepareStatement(sqlService)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    serviceRevenue = rs.getBigDecimal("total");
                    if (serviceRevenue == null) serviceRevenue = BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            log.error("Error getting service revenue: ", e);
        }

        // 2. Doanh thu từ PHỤ PHÍ (SurchargeDetail)
        BigDecimal surchargeRevenue = BigDecimal.ZERO;
        String sqlSurcharge = "SELECT ISNULL(SUM(s.price * sd.quantity), 0) as total " +
                             "FROM SurchargeDetail sd " +
                             "JOIN Surcharge s ON sd.surchargerId = s.surchargeId " +
                             "JOIN Orders o ON sd.orderId = o.orderId " +
                             "WHERE o.paymentDate IS NOT NULL AND CAST(o.paymentDate AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";

        try (PreparedStatement ps = connection.prepareStatement(sqlSurcharge)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    surchargeRevenue = rs.getBigDecimal("total");
                    if (surchargeRevenue == null) surchargeRevenue = BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            log.error("Error getting surcharge revenue: ", e);
        }

        // 3. Doanh thu từ TIỀN PHÒNG (totalAmount - serviceRevenue - surchargeRevenue)
        BigDecimal totalOrderRevenue = BigDecimal.ZERO;
        String sqlTotal = "SELECT ISNULL(SUM(totalAmount), 0) as total " +
                         "FROM Orders " +
                         "WHERE paymentDate IS NOT NULL AND CAST(paymentDate AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";

        try (PreparedStatement ps = connection.prepareStatement(sqlTotal)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalOrderRevenue = rs.getBigDecimal("total");
                    if (totalOrderRevenue == null) totalOrderRevenue = BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            log.error("Error getting total order revenue: ", e);
        }

        // Tiền phòng = Tổng - Dịch vụ - Phụ phí
        BigDecimal roomRevenue = totalOrderRevenue.subtract(serviceRevenue).subtract(surchargeRevenue);
        if (roomRevenue.compareTo(BigDecimal.ZERO) < 0) {
            roomRevenue = BigDecimal.ZERO;
        }

        // Tổng doanh thu
        totalRevenue = totalOrderRevenue;

        // Thêm vào kết quả
        results.add(new RevenueSourceDto("Tiền phòng", roomRevenue, 0));
        results.add(new RevenueSourceDto("Dịch vụ", serviceRevenue, 0));
        results.add(new RevenueSourceDto("Phụ phí", surchargeRevenue, 0));

        // Tính phần trăm
        for (RevenueSourceDto dto : results) {
            if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
                double percentage = dto.getAmount()
                    .divide(totalRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .doubleValue();
                dto.setPercentage(percentage);
            }
        }

        return results;
    }

    /**
     * Lấy khung giờ cao điểm (top 5 giờ có nhiều booking nhất)
     */
    public List<PeakHourDto> getPeakHours(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT TOP 5 DATEPART(HOUR, checkInDate) as hour, COUNT(*) as count " +
                     "FROM Booking " +
                     "WHERE checkInDate BETWEEN ? AND ? " +
                     "GROUP BY DATEPART(HOUR, checkInDate) " +
                     "ORDER BY count DESC";

        List<PeakHourDto> results = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int hour = rs.getInt("hour");
                    int count = rs.getInt("count");
                    results.add(new PeakHourDto(hour + ":00", count));
                }
            }
        } catch (SQLException e) {
            log.error("Error getting peak hours: ", e);
        }

        return results;
    }

    /**
     * Lấy trạng thái phòng hiện tại
     */
    public RoomStatusDto getRoomStatus() {
        RoomStatusDto dto = new RoomStatusDto();

        // Đếm phòng đang sử dụng
        String sql1 = "SELECT COUNT(*) FROM Room WHERE roomStatus = 'OCCUPIED' AND isDeleted = 0";
        try (PreparedStatement ps = connection.prepareStatement(sql1);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                dto.setOccupiedRooms(rs.getInt(1));
            }
        } catch (SQLException e) {
            log.error("Error counting occupied rooms: ", e);
        }

        // Đếm phòng trống
        String sql2 = "SELECT COUNT(*) FROM Room WHERE roomStatus = 'AVAILABLE' AND isDeleted = 0";
        try (PreparedStatement ps = connection.prepareStatement(sql2);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                dto.setAvailableRooms(rs.getInt(1));
            }
        } catch (SQLException e) {
            log.error("Error counting available rooms: ", e);
        }

        // Đếm phòng đã checkout hôm nay
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        String sql3 = "SELECT COUNT(*) FROM Booking " +
                      "WHERE checkOutDate BETWEEN ? AND ? " +
                      "AND checkOutDate IS NOT NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql3)) {
            ps.setTimestamp(1, Timestamp.valueOf(startOfDay));
            ps.setTimestamp(2, Timestamp.valueOf(endOfDay));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto.setCheckedOutRooms(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            log.error("Error counting checked out rooms: ", e);
        }

        // Đếm booking bị hủy hôm nay (giả sử có cột status hoặc cancelledAt)
        // Nếu không có, set = 0
        dto.setCancelledBookings(0);

        return dto;
    }

    /**
     * Lấy cảnh báo
     */
    public WarningDto getWarnings() {
        WarningDto dto = new WarningDto();

        // Đếm phòng trả trễ (checkout quá giờ quy định, giả sử 12:00)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        String sql1 = "SELECT COUNT(*) FROM Booking " +
                      "WHERE checkOutDate BETWEEN ? AND ? " +
                      "AND checkOutDate < ? " +
                      "AND checkOutDate IS NOT NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql1)) {
            ps.setTimestamp(1, Timestamp.valueOf(startOfDay));
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setTimestamp(3, Timestamp.valueOf(LocalDate.now().atTime(12, 0)));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto.setLateCheckOutCount(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            log.error("Error counting late checkouts: ", e);
        }

        // Đếm phòng hỏng
        String sql2 = "SELECT COUNT(*) FROM Room " +
                      "WHERE roomStatus = 'OUT_OF_ORDER' AND isDeleted = 0";
        try (PreparedStatement ps = connection.prepareStatement(sql2);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                dto.setBrokenRoomsCount(rs.getInt(1));
            }
        } catch (SQLException e) {
            log.error("Error counting broken rooms: ", e);
        }

        // Phiên bản mới - hardcode false
        dto.setHasNewVersion(false);

        return dto;
    }

    /**
     * Lấy số phòng đang được sử dụng theo loại phòng (SINGLE hoặc DOUBLE)
     * @param roomTypeId "SINGLE" hoặc "DOUBLE"
     * @return Số phòng đang sử dụng
     */
    public int getOccupiedRoomsByType(String roomTypeId) {
        String sql = "SELECT COUNT(*) as occupied " +
                     "FROM Room " +
                     "WHERE roomStatus = 'OCCUPIED' " +
                     "AND isDeleted = 0 " +
                     "AND roomTypeId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, roomTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("occupied");
                }
            }
        } catch (SQLException e) {
            log.error("Error getting occupied rooms by type {}: ", roomTypeId, e);
        }
        return 0;
    }

    /**
     * Lấy tổng số phòng theo loại (SINGLE hoặc DOUBLE)
     * @param roomTypeId "SINGLE" hoặc "DOUBLE"
     * @return Tổng số phòng
     */
    public int getTotalRoomsByType(String roomTypeId) {
        String sql = "SELECT COUNT(*) as total " +
                     "FROM Room " +
                     "WHERE isDeleted = 0 " +
                     "AND roomTypeId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, roomTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            log.error("Error getting total rooms by type {}: ", roomTypeId, e);
        }
        return 0;
    }

    /**
     * Đếm số phòng sắp hết hạn (checkout trong vòng 2 giờ tới)
     */
    private int countRoomsNearExpiry(LocalDateTime fromTime, LocalDateTime toTime) {
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
     * Đếm tổng số phòng trong hệ thống
     */
    private int countTotalRooms() {
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
     * Đếm số lượt check-in trong khoảng thời gian
     */
    private int countCheckIns(LocalDateTime startDate, LocalDateTime endDate) {
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
    private int countCheckOuts(LocalDateTime startDate, LocalDateTime endDate) {
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
     * Đếm số lượt đặt phòng (Booking) trong khoảng thời gian
     * Đếm tất cả booking được tạo trong khoảng thời gian startDate - endDate
     */
    private int countBookings(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COUNT(*) FROM Booking " +
                     "WHERE createdAt BETWEEN ? AND ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Error counting bookings: ", e);
        }
        return 0;
    }

    /**
     * Lấy tiền mở ca mặc định (5 triệu đồng)
     */
    private BigDecimal getDefaultOpenShiftCash() {
        // Tiền mở ca mặc định là 5 triệu đồng
        return new BigDecimal("5000000");
    }

    /**
     * Lấy tất cả dữ liệu dashboard
     */
    private DashboardSummaryDto getDashboardSummary(LocalDateTime startDate, LocalDateTime endDate) {
        DashboardSummaryDto dto = new DashboardSummaryDto();

        // Lấy số phòng sắp hết hạn (trong 2 giờ tới)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHoursLater = now.plusHours(2);
        dto.setRoomsNearExpiry(countRoomsNearExpiry(now, twoHoursLater));

        // Tổng số phòng
        dto.setTotalRooms(countTotalRooms());

        // Số lượt check-in
        dto.setCheckInCount(countCheckIns(startDate, endDate));

        // Số lượt check-out
        dto.setCheckOutCount(countCheckOuts(startDate, endDate));

        // Số lượt đặt phòng (Booking)
        dto.setBookingCount(countBookings(startDate, endDate));

        // Tiền mở ca mặc định 5 triệu
        dto.setOpenShiftCash(getDefaultOpenShiftCash());

        return dto;
    }

    /**
     * Lấy tổng doanh thu hôm nay
     */
    public BigDecimal getTodayRevenue() {
        // Query doanh thu theo paymentDate (ngày thanh toán)
        String sql = "SELECT ISNULL(SUM(totalAmount), 0) as total " +
                     "FROM Orders " +
                     "WHERE paymentDate = CAST(GETDATE() AS DATE)";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal revenue = rs.getBigDecimal("total");
                return revenue != null ? revenue : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            log.error("Error getting today revenue: ", e);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Lấy doanh thu theo khoảng thời gian (startDate -> endDate)
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Tổng doanh thu trong khoảng thời gian
     */
    public BigDecimal getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT ISNULL(SUM(totalAmount), 0) as total " +
                     "FROM Orders " +
                     "WHERE paymentDate BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal revenue = rs.getBigDecimal("total");
                    return revenue != null ? revenue : BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            log.error("Error getting revenue by date range: ", e);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Đếm số lượng khách hiện đang ở trong khách sạn
     */
    public int getCurrentGuestCount() {
        String sql = "SELECT COUNT(DISTINCT o.customerId) as guestCount " +
                     "FROM Orders o " +
                     "JOIN Booking b ON o.orderId = b.orderId " +
                     "WHERE b.checkInDate <= GETDATE() " +
                     "AND (b.checkOutDate IS NULL OR b.checkOutDate > GETDATE()) " +
                     "AND o.orderTypeId = 2";  // Đơn đang xử lý

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("guestCount");
            }
        } catch (SQLException e) {
            log.error("Error getting current guest count: ", e);
        }
        return 0;
    }

    /**
     * Lấy tổng số phòng trong khách sạn
     */
    public int getTotalRooms() {
        String sql = "SELECT COUNT(*) as total FROM Room";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            log.error("Error getting total rooms: ", e);
        }
        return 0;
    }

    /**
     * Lấy số phòng đang được sử dụng (roomStatus = 'OCCUPIED')
     */
    public int getOccupiedRooms() {
        String sql = "SELECT COUNT(*) as occupied " +
                     "FROM Room " +
                     "WHERE roomStatus = 'OCCUPIED' AND isDeleted = 0";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("occupied");
            }
        } catch (SQLException e) {
            log.error("Error getting occupied rooms: ", e);
        }
        return 0;
    }

    /**
     * Lấy tổng doanh thu từ PHÒNG hôm nay
     */
    public BigDecimal getTotalRoomRevenue() {
        String sql = "SELECT ISNULL(SUM(o.totalAmount - " +
                     "    ISNULL((SELECT SUM(s.price * sd.quantity) FROM SurchargeDetail sd " +
                     "            JOIN Surcharge s ON sd.surchargerId = s.surchargeId " +
                     "            WHERE sd.orderId = o.orderId), 0) - " +
                     "    ISNULL((SELECT SUM(od.unitPrice * od.quantity) FROM OrderDetail od WHERE od.orderId = o.orderId), 0)), 0) as roomRevenue " +
                     "FROM Orders o " +
                     "WHERE o.paymentDate = CAST(GETDATE() AS DATE)";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal revenue = rs.getBigDecimal("roomRevenue");
                return revenue != null ? revenue : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            log.error("Error getting total room revenue: ", e);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Đếm số phòng đã bán (đã check-out) hôm nay
     */
    public int getRoomsSoldToday() {
        String sql = "SELECT COUNT(*) as sold " +
                     "FROM Booking " +
                     "WHERE CAST(checkOutDate AS DATE) = CAST(GETDATE() AS DATE)";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("sold");
            }
        } catch (SQLException e) {
            log.error("Error getting rooms sold today: ", e);
        }
        return 0;
    }

    /**
     * Đếm số đơn hàng đã hoàn thành hôm nay (orderTypeId = 1, paymentDate = hôm nay)
     */
    public int getTodayOrderCount() {
        String sql = "SELECT COUNT(*) as orderCount " +
                     "FROM Orders " +
                     "WHERE paymentDate = CAST(GETDATE() AS DATE) " +
                     "AND orderTypeId = 1";  // Đã hoàn thành

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("orderCount");
            }
        } catch (SQLException e) {
            log.error("Error getting today order count: ", e);
        }
        return 0;
    }

    /**
     * Đếm số đơn hàng đã hoàn thành của 1 ngày cụ thể (để so sánh với hôm nay)
     * Dùng cho TimeType.TODAY - so sánh hôm nay với cùng ngày tuần trước
     *
     * @param daysAgo Số ngày trước (ví dụ: 7 = lấy đơn của 7 ngày trước)
     * @return Số đơn hàng của ngày đó
     */
    public int getOrderCountDaysAgo(int daysAgo) {
        String sql = "SELECT COUNT(*) as orderCount " +
                     "FROM Orders " +
                     "WHERE paymentDate = CAST(DATEADD(DAY, ?, GETDATE()) AS DATE) " +
                     "AND orderTypeId = 1";  // Đã hoàn thành

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, -daysAgo);  // Trừ đi số ngày (7 → -7)

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("orderCount");
                }
            }
        } catch (SQLException e) {
            log.error("Error getting order count {} days ago: ", daysAgo, e);
        }
        return 0;
    }

    /**
     * Đếm TỔNG số đơn hàng trong khoảng thời gian (dùng cho DAYS_7, DAYS_30, DAYS_90)
     *
     * @param days Số ngày (7, 30, 90)
     * @param isPrevious true = kỳ trước, false = kỳ hiện tại
     * @return Tổng số đơn hàng
     *
     * Ví dụ với days=7:
     * - isPrevious=false: Đếm từ hôm nay về trước 7 ngày (15/12 → 21/12)
     * - isPrevious=true: Đếm từ 7-14 ngày trước (8/12 → 14/12)
     */
    public int getOrderCountForPeriod(int days, boolean isPrevious) {
        String sql;

        if (isPrevious) {
            // Kỳ trước: từ -(days*2) đến -days
            // Ví dụ days=7: từ -14 đến -7 (8/12 → 14/12)
            sql = "SELECT COUNT(*) as orderCount " +
                  "FROM Orders " +
                  "WHERE paymentDate >= CAST(DATEADD(DAY, ?, GETDATE()) AS DATE) " +
                  "AND paymentDate < CAST(DATEADD(DAY, ?, GETDATE()) AS DATE) " +
                  "AND orderTypeId = 1";
        } else {
            // Kỳ hiện tại: từ -days đến hôm nay
            // Ví dụ days=7: từ -7 đến 0 (15/12 → 21/12)
            sql = "SELECT COUNT(*) as orderCount " +
                  "FROM Orders " +
                  "WHERE paymentDate >= CAST(DATEADD(DAY, ?, GETDATE()) AS DATE) " +
                  "AND paymentDate <= CAST(GETDATE() AS DATE) " +
                  "AND orderTypeId = 1";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (isPrevious) {
                ps.setInt(1, -(days * 2));  // Start: -14, -60, -180
                ps.setInt(2, -days);        // End: -7, -30, -90
            } else {
                ps.setInt(1, -days);        // Start: -7, -30, -90
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("orderCount");
                }
            }
        } catch (SQLException e) {
            log.error("Error getting order count for period {} days (isPrevious={}): ", days, isPrevious, e);
        }
        return 0;
    }
}

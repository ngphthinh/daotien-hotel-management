package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.enums.TimeType;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import iuh.fit.se.group1.repository.interfaces.*;
import iuh.fit.se.group1.repository.jpa.*;
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
public class DashboardService extends Service {

    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);
    //    private final Connection connection;
    private final OrderRepository orderRepository;
    private final ShiftCloseRepository shiftCloseRepository;
    private final OrderDetailRepository orderDetailRepo;
    private final SurchargeDetailRepository surchargeDetailRepo;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public DashboardService() {
//        this.connection = DatabaseUtil.getConnection();
        this.shiftCloseRepository = new ShiftCloseRepositoryImpl();
        this.bookingRepository = new BookingRepositoryImpl();
        this.roomRepository = new RoomRepositoryImpl();
        this.orderRepository = new OrderRepositoryImpl();
        this.orderDetailRepo = new OrderDetailRepositoryImpl();
        this.surchargeDetailRepo = new SurchargeDetailRepositoryImpl();

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
    public List<RevenueSourceDto> getRevenueSources(LocalDateTime start, LocalDateTime end) {

        return doInTransaction(entityManager -> {
            BigDecimal totalRevenue = orderRepository.getTotalOrderRevenue(entityManager, start, end);
            BigDecimal serviceRevenue = orderDetailRepo.getServiceRevenue(entityManager, start, end);
            BigDecimal surchargeRevenue = surchargeDetailRepo.getSurchargeRevenue(entityManager, start, end);
            BigDecimal roomRevenue = totalRevenue.subtract(serviceRevenue).subtract(surchargeRevenue);
            if (roomRevenue.compareTo(BigDecimal.ZERO) < 0) {
                roomRevenue = BigDecimal.ZERO;
            }

            List<RevenueSourceDto> results = new ArrayList<>(List.of(
                    new RevenueSourceDto("Tiền phòng", roomRevenue, 0),
                    new RevenueSourceDto("Dịch vụ", serviceRevenue, 0),
                    new RevenueSourceDto("Phụ phí", surchargeRevenue, 0)
            ));

            if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
                for (RevenueSourceDto dto : results) {
                    double percent = dto.getAmount()
                            .divide(totalRevenue, 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"))
                            .doubleValue();
                    dto.setPercentage(percent);
                }
            }

            return results;
        });
    }

    /**
     * Lấy khung giờ cao điểm (top 5 giờ có nhiều booking nhất)
     */
    public List<PeakHourDto> getPeakHours(LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rows = doInTransaction(entityManager -> bookingRepository.getPeakHours(entityManager, startDate, endDate));

        List<PeakHourDto> results = new ArrayList<>();

        for (Object[] row : rows) {
            int hour = ((Number) row[0]).intValue();
            int count = ((Number) row[1]).intValue();
            results.add(new PeakHourDto(hour + ":00", count));
        }
        return results;
    }

    /**
     * Lấy trạng thái phòng hiện tại
     */
    public RoomStatusDto getRoomStatus() {
        return doInTransaction(em -> {

            RoomStatusDto dto = new RoomStatusDto();

            dto.setOccupiedRooms(roomRepository.countRoomsByStatus(em, RoomStatus.OCCUPIED));
            dto.setAvailableRooms(roomRepository.countRoomsByStatus(em, RoomStatus.AVAILABLE));

            LocalDateTime start = LocalDate.now().atStartOfDay();
            LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

            dto.setCheckedOutRooms(
                    bookingRepository.countCheckedOutToday(em, start, end)
            );

            dto.setCancelledBookings(0);

            return dto;
        });
    }

    /**
     * Lấy cảnh báo
     */
    public WarningDto getWarnings() {
        return doInTransaction(em -> {

            WarningDto dto = new WarningDto();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start = LocalDate.now().atStartOfDay();
            LocalDateTime standard = LocalDate.now().atTime(12, 0);

            dto.setLateCheckOutCount(
                    bookingRepository.countLateCheckout(em, start, now, standard)
            );

            dto.setBrokenRoomsCount(
                    roomRepository.countRoomsByStatus(em, RoomStatus.OUT_OF_ORDER)
            );

            dto.setHasNewVersion(false);

            return dto;
        });
    }

    /**
     * Lấy số phòng đang được sử dụng theo loại phòng (SINGLE hoặc DOUBLE)
     *
     * @param roomTypeId "SINGLE" hoặc "DOUBLE"
     * @return Số phòng đang sử dụng
     */
    public int getOccupiedRoomsByType(String roomTypeId) {
        return doInTransaction(entityManager -> roomRepository.getOccupiedRoomsByType(entityManager, roomTypeId));
    }

    /**
     * Lấy tổng số phòng theo loại (SINGLE hoặc DOUBLE)
     *
     * @param roomTypeId "SINGLE" hoặc "DOUBLE"
     * @return Tổng số phòng
     */
//    public int getTotalRoomsByType(String roomTypeId) {
//        String sql = "SELECT COUNT(*) as total " +
//                "FROM Room " +
//                "WHERE isDeleted = 0 " +
//                "AND roomTypeId = ?";
//
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, roomTypeId);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getInt("total");
//                }
//            }
//        } catch (SQLException e) {
//            log.error("Error getting total rooms by type {}: ", roomTypeId, e);
//        }
//        return 0;
//    }

    /**
     * Đếm số phòng sắp hết hạn (checkout trong vòng 2 giờ tới)
     */
    private int countRoomsNearExpiry(LocalDateTime fromTime, LocalDateTime toTime) {
        return doInTransaction(entityManager -> bookingRepository.countRoomsNearExpiry(entityManager, fromTime, toTime));
    }

    /**
     * Đếm tổng số phòng trong hệ thống
     */
    private int countTotalRooms() {
        return doInTransaction(roomRepository::count);
    }

    /**
     * Đếm số lượt check-in trong khoảng thời gian
     */
    private int countCheckIns(LocalDateTime startDate, LocalDateTime endDate) {
        return doInTransaction(entityManager -> bookingRepository.countCheckIns(entityManager, startDate, endDate));
    }

    /**
     * Đếm số lượt check-out trong khoảng thời gian
     */
    private int countCheckOuts(LocalDateTime startDate, LocalDateTime endDate) {
        return doInTransaction(entityManager -> bookingRepository.countCheckOuts(entityManager, startDate, endDate));
    }

    /**
     * Đếm số lượt đặt phòng (Booking) trong khoảng thời gian
     * Đếm tất cả booking được tạo trong khoảng thời gian startDate - endDate
     */
    private int countBookings(LocalDateTime startDate, LocalDateTime endDate) {
        return doInTransaction(entityManager -> bookingRepository.countBookings(entityManager, startDate, endDate));
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
//    public BigDecimal getTodayRevenue() {
//        // Query doanh thu theo paymentDate (ngày thanh toán)
//        String sql = "SELECT ISNULL(SUM(totalAmount), 0) as total " +
//                "FROM Orders " +
//                "WHERE paymentDate = CAST(GETDATE() AS DATE)";
//
//        try (PreparedStatement ps = connection.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) {
//                BigDecimal revenue = rs.getBigDecimal("total");
//                return revenue != null ? revenue : BigDecimal.ZERO;
//            }
//        } catch (SQLException e) {
//            log.error("Error getting today revenue: ", e);
//        }
//        return BigDecimal.ZERO;
//    }

    /**
     * Lấy doanh thu theo khoảng thời gian (startDate -> endDate)
     *
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Tổng doanh thu trong khoảng thời gian
     */
    public BigDecimal getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return doInTransaction(em ->
                orderRepository.getRevenueByDateRange(em, startDate, endDate)
        );
    }

    /**
     * Đếm số lượng khách hiện đang ở trong khách sạn
     */
    public int getCurrentGuestCount() {
        return doInTransaction(orderRepository::getCurrentGuestCount
        );
    }

    /**
     * Lấy tổng số phòng trong khách sạn
     */
    public int getTotalRooms() {
        return doInTransaction(roomRepository::count);
    }

    /**
     * Lấy số phòng đang được sử dụng (roomStatus = 'OCCUPIED')
     */
    public int getOccupiedRooms() {
        return doInTransaction(entityManager -> roomRepository.countRoomsByStatus(entityManager, RoomStatus.OCCUPIED));
    }

    /**
     * Lấy tối đa 2 ghi chú ca làm việc gần nhất từ ShiftClose
     *
     * @return Danh sách ghi chú ca làm việc (tối đa 2 ca)
     */
    public List<ShiftNoteDto> getRecentShiftNotes() {
        return doInTransaction(em -> {
            List<Object[]> rows = shiftCloseRepository.getRecentShiftNotes(em);

            List<ShiftNoteDto> result = new ArrayList<>();
            for (Object[] r : rows) {
                String employeeName = (String) r[0];
                String shiftName = (String) r[1];
                LocalDate shiftDate = (LocalDate) r[2];
                String note = (String) r[3];

                result.add(new ShiftNoteDto(
                        employeeName,
                        shiftName,
                        shiftDate != null ? shiftDate.atStartOfDay() : null,
                        note
                ));
            }
            return result;
        });
    }

    /**
     * Lấy tổng doanh thu từ PHÒNG hôm nay
     */
//    public BigDecimal getTotalRoomRevenue() {
//        String sql = "SELECT ISNULL(SUM(o.totalAmount - " +
//                "    ISNULL((SELECT SUM(s.price * sd.quantity) FROM SurchargeDetail sd " +
//                "            JOIN Surcharge s ON sd.surchargerId = s.surchargeId " +
//                "            WHERE sd.orderId = o.orderId), 0) - " +
//                "    ISNULL((SELECT SUM(od.unitPrice * od.quantity) FROM OrderDetail od WHERE od.orderId = o.orderId), 0)), 0) as roomRevenue " +
//                "FROM Orders o " +
//                "WHERE o.paymentDate = CAST(GETDATE() AS DATE)";
//
//        try (PreparedStatement ps = connection.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) {
//                BigDecimal revenue = rs.getBigDecimal("roomRevenue");
//                return revenue != null ? revenue : BigDecimal.ZERO;
//            }
//        } catch (SQLException e) {
//            log.error("Error getting total room revenue: ", e);
//        }
//        return BigDecimal.ZERO;
//    }

    /**
     * Đếm số phòng đã bán (đã check-out) hôm nay
     */
//    public int getRoomsSoldToday() {
//        String sql = "SELECT COUNT(*) as sold " +
//                "FROM Booking " +
//                "WHERE CAST(checkOutDate AS DATE) = CAST(GETDATE() AS DATE)";
//
//        try (PreparedStatement ps = connection.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) {
//                return rs.getInt("sold");
//            }
//        } catch (SQLException e) {
//            log.error("Error getting rooms sold today: ", e);
//        }
//        return 0;
//    }

    /**
     * Đếm số đơn hàng đã hoàn thành hôm nay (orderTypeId = 1, paymentDate = hôm nay)
     */
    public int getTodayOrderCount() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        return doInTransaction(em ->
                orderRepository.getOrderCountByDate(em, start, end)
        );
    }

    /**
     * Đếm số đơn hàng đã hoàn thành của 1 ngày cụ thể (để so sánh với hôm nay)
     * Dùng cho TimeType.TODAY - so sánh hôm nay với cùng ngày tuần trước
     *
     * @param daysAgo Số ngày trước (ví dụ: 7 = lấy đơn của 7 ngày trước)
     * @return Số đơn hàng của ngày đó
     */
    public int getOrderCountDaysAgo(int daysAgo) {
        LocalDate targetDate = LocalDate.now().minusDays(daysAgo);

        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.atTime(LocalTime.MAX);

        return doInTransaction(em ->
                orderRepository.getOrderCountByDate(em, start, end)
        );
    }

    public int getOrderCountForPeriod(int days, boolean isPrevious) {

        LocalDate today = LocalDate.now();

        LocalDate startDate;
        LocalDate endDate;

        if (isPrevious) {
            startDate = today.minusDays(days * 2L);
            endDate = today.minusDays(days).minusDays(1);
        } else {
            startDate = today.minusDays(days);
            endDate = today;
        }

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        return doInTransaction(em ->
                orderRepository.getOrderCountByDate(em, start, end)
        );
    }
}

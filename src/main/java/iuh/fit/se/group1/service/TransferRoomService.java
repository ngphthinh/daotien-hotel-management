package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.RoomDTO;
import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.repository.OrderRepository;
import iuh.fit.se.group1.repository.TransferRoomRepository;
import iuh.fit.se.group1.repository.TransferRoomRepository.BookingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class TransferRoomService {
    private static final Logger log = LoggerFactory.getLogger(TransferRoomService.class);
    private static final String SINGLE_ROOM_TYPE = "SINGLE";
    private static final String DOUBLE_ROOM_TYPE = "DOUBLE";

    private final TransferRoomRepository repository;
    private final OrderRepository orderRepository;
    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    public TransferRoomService() {
        this.repository = new TransferRoomRepository();
        this.orderRepository = new OrderRepository();
        this.roomService = new RoomService();
        this.roomTypeService = new RoomTypeService();
    }

    /**
     * Lấy tất cả bookings đang hoạt động
     */

    public List<BookingDTO> getAllBookings() {
        return repository.getAllBookings();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllOrders();
    }

    /**
     * Tìm kiếm booking theo CCCD
     */
    public List<BookingDTO> searchBookings(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBookings();
        }
        return repository.searchBookingsByCitizenId(keyword.trim());
    }

    /**
     * Lấy thông tin TẤT CẢ phòng theo orderId và bookingType
     */
    public List<Room> getRoomsByOrderAndType(long orderId, BookingType bookingType) {
        return repository.getRoomsByOrderIdAndType(orderId, bookingType.name());
    }

    /**
     * Lấy thông tin phòng theo booking (giữ lại cho tương thích)
     */
    public List<Room> getRoomsByBooking(long bookingId) {
        return repository.getRoomsByBookingId(bookingId);
    }

    /**
     * Lấy phòng trống theo loại
     */
    public List<Room> getAvailableRoomsByType(String roomTypeId) {
        return repository.getAvailableRoomsByType(roomTypeId);
    }
    
    /**
     * Tính số lượng thời gian thuê theo loại booking
     */
    private long calculateBookingDuration(LocalDateTime checkIn, LocalDateTime checkOut, BookingType bookingType) {
        switch (bookingType) {
            case HOURLY:
                // Tính số giờ, làm tròn lên
                long hours = Duration.between(checkIn, checkOut).toHours();
                long minutes = Duration.between(checkIn, checkOut).toMinutes() % 60;
                return minutes > 0 ? hours + 1 : hours;

            case DAILY:
                // Tính số ngày thuê: mốc 12h trưa
                // Check-out trước 12h -> không tính ngày đó
                // Check-out từ 12h trở đi -> tính ngày đó
                // VD: 08/12 14:00 -> 12/12 12:00 = 4 ngày (8,9,10,11)
                // 08/12 14:00 -> 12/12 14:00 = 5 ngày (8,9,10,11,12)
                long days = ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());

                // Nếu check-out từ 12h trở đi, cộng thêm 1 ngày
                if (checkOut.getHour() >= 12) {
                    days++;
                }

                return Math.max(1, days);

            case OVERNIGHT:
                return 1;

            default:
                return 1;
        }
    }

    /**
     * Tính phụ phí chuyển phòng với thông tin booking
     */
    public long calculateSurcharge(List<Room> oldRooms, List<Room> newRooms,
            BookingType bookingType, long orderId) {
        // Validate input lists
        if (oldRooms == null || oldRooms.isEmpty() || newRooms == null || newRooms.isEmpty()) {
            log.warn("Empty room lists provided for surcharge calculation");
            return 0;
        }

        // Lấy thông tin booking để biết thời gian thuê
        Booking bookingInfo = repository.getBookingByOrderIdAndType(
                orderId, bookingType.name(), oldRooms.get(0).getRoomId());

        if (bookingInfo == null) {
            log.error("Cannot find booking info for orderId={}, bookingType={}", orderId, bookingType);
            return 0;
        }

        // Tính số lượng thời gian thuê
        long duration = calculateBookingDuration(
                bookingInfo.getCheckInDate(),
                bookingInfo.getCheckOutDate(),
                bookingType);

        log.info("Booking duration: {} {} for bookingType={}",
                duration, bookingType.getDisplayName(), bookingType);

        // Tính tổng giá với duration
        long oldTotal = calculateTotalPrice(oldRooms, bookingType, (int) duration);
        long newTotal = calculateTotalPrice(newRooms, bookingType, (int) duration);

        log.info("Old total: {}, New total: {}, Surcharge: {}",
                oldTotal, newTotal, newTotal - oldTotal);

        return newTotal - oldTotal;
    }

    /**
     * Tính tổng giá phòng theo loại thuê và duration
     */
    private long calculateTotalPrice(List<Room> rooms, BookingType bookingType, int duration) {
        int bookingTypeIndex = BookingType.toIndex(bookingType);

        RoomType singleRoomType = roomTypeService.getRoomTypeById(SINGLE_ROOM_TYPE);
        RoomType doubleRoomType = roomTypeService.getRoomTypeById(DOUBLE_ROOM_TYPE);

        RoomDTO singleRoomDTO = roomService.toRoomDTO(singleRoomType);
        RoomDTO doubleRoomDTO = roomService.toRoomDTO(doubleRoomType);

        long singleCount = rooms.stream()
                .filter(room -> SINGLE_ROOM_TYPE.equals(room.getRoomType().getRoomTypeId()))
                .count();

        long doubleCount = rooms.stream()
                .filter(room -> DOUBLE_ROOM_TYPE.equals(room.getRoomType().getRoomTypeId()))
                .count();

        long totalPrice = 0L;
        if (singleCount > 0) {
            totalPrice += calculatePriceByTypeAndDuration(bookingTypeIndex, singleRoomDTO, duration) * singleCount;
        }
        if (doubleCount > 0) {
            totalPrice += calculatePriceByTypeAndDuration(bookingTypeIndex, doubleRoomDTO, duration) * doubleCount;
        }

        return totalPrice;
    }

    /**
     * Lấy giá phòng theo loại thuê (đơn giá cơ bản)
     */
    public long getRoomPriceByType(Room room, BookingType bookingType) {
        RoomType roomType = room.getRoomType();
        if (roomType == null) {
            return 0;
        }

        BigDecimal price = switch (bookingType) {
            case HOURLY -> roomType.getHourlyRate();
            case DAILY -> roomType.getDailyRate();
            case OVERNIGHT -> roomType.getOvernightRate();
        };

        return price != null ? price.longValue() : 0;
    }

    private long calculatePriceByTypeAndDuration(int bookingTypeIndex, RoomDTO roomDTO, int duration) {

        System.out.println(roomDTO + ", duration: " + duration + ", bookingTypeIndex: " + bookingTypeIndex);

        return switch (bookingTypeIndex) {
            case 0 -> // Theo giờ: giá giờ đầu + giá giờ thêm × (số giờ - 1)
                roomDTO.getHourlyRate().longValueExact()
                        + ((duration - 1) * roomDTO.getAdditionalHourRate().longValueExact());
            case 1 -> // Theo ngày: giá theo ngày × số ngày
                roomDTO.getDailyRate().longValueExact() * duration;
            case 2 -> // Qua đêm: giá cố định
                roomDTO.getOvernightRate().longValueExact();
            default -> 0L;
        };
    }

    /**
     * Lấy giá phòng đã tính duration để hiển thị (cho UI)
     */
    public long getRoomPriceWithDuration(Room room, BookingType bookingType, long orderId) {
        Booking bookingInfo = repository.getBookingByOrderIdAndType(
                orderId, bookingType.name(), room.getRoomId());

        if (bookingInfo == null) {
            return getRoomPriceByType(room, bookingType);
        }

        long duration = calculateBookingDuration(
                bookingInfo.getCheckInDate(),
                bookingInfo.getCheckOutDate(),
                bookingType);

        RoomDTO roomDTO = roomService.toRoomDTO(room.getRoomType());



        int bookingTypeIndex = BookingType.toIndex(bookingType);
        return calculatePriceByTypeAndDuration(bookingTypeIndex, roomDTO, (int) duration);
    }

    /**
     * Thực hiện chuyển phòng
     */
    public TransferResult transferRooms(long orderId,
            List<Room> oldRooms,
            List<Room> newRooms,
            BookingType bookingType) {

        TransferResult result = new TransferResult();

        // Validate
        if (oldRooms.isEmpty() || newRooms.isEmpty()) {
            result.success = false;
            result.message = "Danh sách phòng cũ hoặc phòng mới trống";
            return result;
        }

        // Tính phụ phí với duration
        long surcharge = calculateSurcharge(oldRooms, newRooms, bookingType, orderId);
        result.surcharge = surcharge;

        // Lấy danh sách ID
        List<Long> oldRoomIds = oldRooms.stream()
                .map(Room::getRoomId)
                .collect(Collectors.toList());

        List<Long> newRoomIds = newRooms.stream()
                .map(Room::getRoomId)
                .collect(Collectors.toList());

        // Thực hiện chuyển phòng với orderId và bookingType
        boolean transferSuccess = repository.transferRooms(orderId, bookingType.name(), oldRoomIds, newRoomIds);

        if (!transferSuccess) {
            result.success = false;
            result.message = "Lỗi khi chuyển phòng trong database";
            return result;
        }

        // Cập nhật phụ phí vào order
        if (surcharge != 0) {
            boolean surchargeSuccess = repository.addSurchargeToOrder(orderId, surcharge);
            if (!surchargeSuccess) {
                log.warn("Failed to add surcharge to order {}", orderId);
            }
        }

        result.success = true;
        result.message = "Chuyển phòng thành công";
        return result;
    }

    /**
     * Validate có thể chuyển phòng không
     * CHỈ CHO PHÉP các trường hợp sau:
     * 1. 1 đôi → 2 đơn (chia phòng)
     * 2. 2 đơn → 1 đôi (gộp phòng)
     * 3. 1 đôi → 1 đôi (đổi phòng cùng loại)
     * 4. 1 đơn → 1 đôi (nâng cấp)
     * 5. 1 đơn → 1 đơn (đổi phòng cùng loại)
     * Các trường hợp khác đều KHÔNG hợp lệ
     */
    public ValidationResult validateTransfer(List<Room> oldRooms,
            List<Room> newRooms,
            BookingType bookingType) {
        ValidationResult result = new ValidationResult();

        if (oldRooms.isEmpty()) {
            result.valid = false;
            result.message = "Chưa chọn phòng cũ";
            return result;
        }

        if (newRooms.isEmpty()) {
            result.valid = false;
            result.message = "Chưa chọn phòng mới";
            return result;
        }

        // Đếm số phòng đơn và đôi trong phòng cũ
        long oldSingleCount = oldRooms.stream()
                .filter(r -> SINGLE_ROOM_TYPE.equals(r.getRoomType().getRoomTypeId()))
                .count();
        long oldDoubleCount = oldRooms.stream()
                .filter(r -> DOUBLE_ROOM_TYPE.equals(r.getRoomType().getRoomTypeId()))
                .count();

        // Đếm số phòng đơn và đôi trong phòng mới
        long newSingleCount = newRooms.stream()
                .filter(r -> SINGLE_ROOM_TYPE.equals(r.getRoomType().getRoomTypeId()))
                .count();
        long newDoubleCount = newRooms.stream()
                .filter(r -> DOUBLE_ROOM_TYPE.equals(r.getRoomType().getRoomTypeId()))
                .count();

        // TRƯỜNG HỢP 1: 1 đôi → 2 đơn
        if (oldRooms.size() == 1 && oldDoubleCount == 1 &&
                newRooms.size() == 2 && newSingleCount == 2) {
            result.valid = true;
            result.message = "Hợp lệ: 1 phòng đôi → 2 phòng đơn";
            return result;
        }

        // TRƯỜNG HỢP 2: 2 đơn → 1 đôi
        if (oldRooms.size() == 2 && oldSingleCount == 2 &&
                newRooms.size() == 1 && newDoubleCount == 1) {
            result.valid = true;
            result.message = "Hợp lệ: 2 phòng đơn → 1 phòng đôi";
            return result;
        }

        // TRƯỜNG HỢP 3: 1 đôi → 1 đôi
        if (oldRooms.size() == 1 && oldDoubleCount == 1 &&
                newRooms.size() == 1 && newDoubleCount == 1) {
            result.valid = true;
            result.message = "Hợp lệ: 1 phòng đôi → 1 phòng đôi";
            return result;
        }

        // TRƯỜNG HỢP 4: 1 đơn → 1 đôi
        if (oldRooms.size() == 1 && oldSingleCount == 1 &&
                newRooms.size() == 1 && newDoubleCount == 1) {
            result.valid = true;
            result.message = "Hợp lệ: 1 phòng đơn → 1 phòng đôi";
            return result;
        }

        // TRƯỜNG HỢP 5: 1 đơn → 1 đơn
        if (oldRooms.size() == 1 && oldSingleCount == 1 &&
                newRooms.size() == 1 && newSingleCount == 1) {
            result.valid = true;
            result.message = "Hợp lệ: 1 phòng đơn → 1 phòng đơn";
            return result;
        }

        // TẤT CẢ các trường hợp khác đều KHÔNG hợp lệ
        result.valid = false;
        result.message = String.format(
                "<b><span style='color: red; font-size: 18px;'>Không hỗ trợ chuyển đổi này!</span></b><br>" +
                        "%d phòng cũ (%d đơn, %d đôi) → %d phòng mới (%d đơn, %d đôi)<br>" +
                        "<b>Các trường hợp được phép:</b><br>" +
                        "1 phòng đôi → 2 phòng đơn<br>" +
                        "2 phòng đơn → 1 phòng đôi<br>" +
                        "1 phòng đôi → 1 phòng đôi<br>" +
                        "1 phòng đơn → 1 phòng đôi<br>" +
                        "1 phòng đơn → 1 phòng đơn",
                oldRooms.size(), oldSingleCount, oldDoubleCount,
                newRooms.size(), newSingleCount, newDoubleCount);
        return result;
    }

    public List<Order> findOrdersUnPendingByKeyWord(String keyword) {
        return orderRepository.findOrdersUnPendingByKeyWord(keyword);
    }

    // Result classes
    public static class TransferResult {
        public boolean success;
        public String message;
        public long surcharge;
    }

    public static class ValidationResult {
        public boolean valid;
        public String message;
    }

public boolean cancelRoomBooking(Long orderId, String roomNumber, BookingType bookingType) {
    try {
        log.info("Canceling room booking - OrderId: {}, RoomNumber: {}, BookingType: {}", 
                orderId, roomNumber, bookingType);
        
        // Gọi repository để hủy booking
        boolean success = repository.cancelRoomBooking(orderId, roomNumber, bookingType.name());
        
        if (success) {
            log.info("Successfully cancelled room booking");
        } else {
            log.warn("Failed to cancel room booking");
        }
        
        return success;
    } catch (Exception e) {
        log.error("Error canceling room booking: {}", e.getMessage(), e);
        return false;
    }
}

public boolean extendRoomBooking(Long orderId, List<Room> rooms, int days, BookingType bookingType) {
    try {
        log.info("Extending room booking - OrderId: {}, Rooms: {}, Days: {}, BookingType: {}", 
                orderId, rooms.size(), days, bookingType);
        
        if (rooms == null || rooms.isEmpty()) {
            log.warn("No rooms provided for extension");
            return false;
        }
        
        if (days <= 0) {
            log.warn("Invalid number of days: {}", days);
            return false;
        }
        
        // Lấy danh sách room IDs
        List<Long> roomIds = rooms.stream()
                .map(Room::getRoomId)
                .collect(Collectors.toList());
        
        // Gọi repository để gia hạn
        boolean success = repository.extendRoomBooking(orderId, roomIds, days, bookingType.name());
        
        if (success) {
            log.info("Successfully extended room booking");
            
            // Tính thêm tiền cho số ngày gia hạn
            long additionalCharge = calculateExtensionCharge(rooms, days, bookingType);
            
            if (additionalCharge > 0) {
                // Cập nhật tổng tiền vào order
                repository.addSurchargeToOrder(orderId, additionalCharge);
                log.info("Added extension charge: {}", additionalCharge);
            }
        } else {
            log.warn("Failed to extend room booking");
        }
        
        return success;
    } catch (Exception e) {
        log.error("Error extending room booking: {}", e.getMessage(), e);
        return false;
    }
}

/**
 * Tính tiền phụ thu cho việc gia hạn
 */
private long calculateExtensionCharge(List<Room> rooms, int days, BookingType bookingType) {
    if (bookingType != BookingType.DAILY) {
        return 0; // Chỉ tính phí gia hạn cho thuê theo ngày
    }
    
    return calculateTotalPrice(rooms, bookingType, days);
}
}
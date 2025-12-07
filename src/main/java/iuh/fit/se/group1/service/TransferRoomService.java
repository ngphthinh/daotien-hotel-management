package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.repository.TransferRoomRepository;
import iuh.fit.se.group1.repository.TransferRoomRepository.BookingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class TransferRoomService {
    private static final Logger log = LoggerFactory.getLogger(TransferRoomService.class);
    private final TransferRoomRepository repository;

    public TransferRoomService() {
        this.repository = new TransferRoomRepository();
    }

    /**
     * Lấy tất cả bookings đang hoạt động
     */
    public List<BookingDTO> getAllBookings() {
        return repository.getAllBookings();
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
     * Tính phụ phí chuyển phòng
     */
    public long calculateSurcharge(List<Room> oldRooms, List<Room> newRooms, BookingType bookingType) {
        long oldTotal = calculateTotalPrice(oldRooms, bookingType);
        long newTotal = calculateTotalPrice(newRooms, bookingType);
        return newTotal - oldTotal;
    }

    /**
     * Tính tổng giá phòng theo loại thuê
     */
    private long calculateTotalPrice(List<Room> rooms, BookingType bookingType) {
        return rooms.stream()
                .mapToLong(room -> getRoomPriceByType(room, bookingType))
                .sum();
    }

    /**
     * Lấy giá phòng theo loại thuê
     */
    public long getRoomPriceByType(Room room, BookingType bookingType) {
        RoomType roomType = room.getRoomType();
        if (roomType == null)
            return 0;

        BigDecimal price = switch (bookingType) {
            case HOURLY -> roomType.getHourlyRate();
            case DAILY -> roomType.getDailyRate();
            case OVERNIGHT -> roomType.getOvernightRate();
        };

        return price != null ? price.longValue() : 0;
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

        // Tính phụ phí
        long surcharge = calculateSurcharge(oldRooms, newRooms, bookingType);
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
                .filter(r -> "SINGLE".equals(r.getRoomType().getRoomTypeId()))
                .count();
        long oldDoubleCount = oldRooms.stream()
                .filter(r -> "DOUBLE".equals(r.getRoomType().getRoomTypeId()))
                .count();

        // Đếm số phòng đơn và đôi trong phòng mới
        long newSingleCount = newRooms.stream()
                .filter(r -> "SINGLE".equals(r.getRoomType().getRoomTypeId()))
                .count();
        long newDoubleCount = newRooms.stream()
                .filter(r -> "DOUBLE".equals(r.getRoomType().getRoomTypeId()))
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

    /**
     * Format loại booking cho hiển thị
     */
    public String getBookingTypeDisplay(BookingType bookingType) {
        return switch (bookingType) {
            case HOURLY -> "Theo giờ";
            case DAILY -> "Theo ngày";
            case OVERNIGHT -> "Qua đêm";
        };
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
}
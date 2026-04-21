package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.dto.BookingDTO;
import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository {
    boolean isExistsByRoomAndDate(Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate);

    void saveAllBookingsForOrder(Order savedOrder, List<Booking> bookings);

    void removeBookingsFromOrder(Order currentOrder, List<Booking> result);

    void moveBookingsToOrder(Long targetOrderId, List<Long> bookingIds);

    void updateBookingDates(Long bookingId, LocalDateTime checkIn, LocalDateTime checkOut);

    void deleteByOrderId(Long id);

    List<Booking> findByOrderId(Long orderId);

    int countRoomsNearExpiry(LocalDateTime fromTime, LocalDateTime toTime);

    int countCheckIns(LocalDateTime startDate, LocalDateTime endDate);

    int countCheckOuts(LocalDateTime startDate, LocalDateTime endDate);

    int countCheckedOutRooms(LocalDateTime startDate, LocalDateTime endDate);

    int countLateCheckOuts(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime deadlineTime);

    List<BookingDisplayDTO> findAllBookingDisplay();

    List<BookingDTO> getAllBookings();

    Booking getBookingById(long bookingId, long roomId);

    boolean extendRoomBooking(Long orderId, List<Long> roomIds,
                              int extendValue, String bookingType);


    boolean cancelRoomBooking(Long orderId, Long roomId, String bookingType);


    Booking getBookingByOrderIdAndType(long orderId, String bookingType, long roomId);

    List<BookingDTO> searchBookingsByCitizenId(String citizenId);

}

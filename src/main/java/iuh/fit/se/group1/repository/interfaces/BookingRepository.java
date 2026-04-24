package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.dto.BookingDTO;
import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Order;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository {
    boolean isExistsByRoomAndDate(EntityManager em, Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate);

    void saveAllBookingsForOrder(EntityManager em, Order savedOrder, List<Booking> bookings);

    void removeBookingsFromOrder(EntityManager em, Order currentOrder, List<Booking> result);

    void moveBookingsToOrder(EntityManager em, Long targetOrderId, List<Long> bookingIds);

    void updateBookingDates(EntityManager em, Long bookingId, LocalDateTime checkIn, LocalDateTime checkOut);

    void deleteByOrderId(EntityManager em, Long id);

    List<Booking> findByOrderId(EntityManager em, Long orderId);

    int countRoomsNearExpiry(EntityManager em, LocalDateTime fromTime, LocalDateTime toTime);

    int countCheckIns(EntityManager em, LocalDateTime startDate, LocalDateTime endDate);

    int countCheckOuts(EntityManager em, LocalDateTime startDate, LocalDateTime endDate);

    int countCheckedOutRooms(EntityManager em, LocalDateTime startDate, LocalDateTime endDate);

    int countLateCheckOuts(EntityManager em, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime deadlineTime);

    List<BookingDisplayDTO> findAllBookingDisplay(EntityManager em);

    List<BookingDTO> getAllBookings(EntityManager em);

    Booking getBookingById(EntityManager em, long bookingId, long roomId);

    boolean extendRoomBooking(EntityManager em, Long orderId, List<Long> roomIds,
                              int extendValue, String bookingType);


    boolean cancelRoomBooking(EntityManager em, Long orderId, Long roomId, String bookingType);


    Booking getBookingByOrderIdAndType(EntityManager em, long orderId, String bookingType, long roomId);

    List<BookingDTO> searchBookingsByCitizenId(EntityManager em, String citizenId);

}

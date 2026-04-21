package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.dto.BookingDTO;
import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Room;

import java.util.List;

public interface RoomToolsRepository {

    List<BookingDTO> getAllBookings();

    List<BookingDTO> searchBookingsByCitizenId(String citizenId);

    List<Room> getRoomsByOrderIdAndType(long orderId, String bookingType);

    List<Room> getRoomsByBookingId(long bookingId);

    List<Room> getAvailableRoomsByType(String roomTypeId);

    Booking getBookingById(long bookingId, long roomId);

    boolean transferRooms(long orderId, String bookingType, List<Long> oldRoomIds, List<Long> newRoomIds);

    Booking getBookingByOrderIdAndType(long orderId, String bookingType, long roomId);

    boolean addSurchargeToOrder(long orderId, long surchargeAmount);

    boolean existsTransformType(Long orderId);

    boolean cancelRoomBooking(Long orderId, Long roomId, String bookingType);

    boolean extendRoomBooking(Long orderId, List<Long> roomIds,
                              int extendValue, String bookingType);

    boolean addRoomAmountToOrder(long orderId, long amount);

    boolean subtractAmountFromOrder(long orderId, double amount);
}

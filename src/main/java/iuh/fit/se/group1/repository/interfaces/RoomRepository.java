package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.enums.RoomStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository {
    List<Room> findByRoomNumberOrId(String keyword);

    List<Room> findRoomByStatusAndRoomType(String roomTypeId, RoomStatus roomStatus);

    void updateRoomStatusBatch(List<Long> roomsIdx, RoomStatus roomStatus);

    List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, RoomStatus roomStatus);

    boolean existsByRoomNumber(String roomNumber);

    int countTotalRooms();

    int countByStatus(RoomStatus status);

    void updateBookingRoom(Long bookingId, Long newRoomId);

    boolean isRoomInUse(Long roomId);

    List<Room> getRoomsByOrderIdAndType(long orderId, String bookingType);

    List<Room> getRoomsByBookingId(long bookingId);

    List<Room> getAvailableRoomsByType(String roomTypeId);

    boolean transferRooms(long orderId, String bookingType, List<Long> oldRoomIds, List<Long> newRoomIds);


}

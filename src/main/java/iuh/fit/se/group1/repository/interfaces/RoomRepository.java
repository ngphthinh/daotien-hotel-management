package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.enums.RoomStatus;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository {
    List<Room> findByRoomNumberOrId(EntityManager em, String keyword);

    List<Room> findRoomByStatusAndRoomType(EntityManager em, String roomTypeId, RoomStatus roomStatus);

    void updateRoomStatusBatch(EntityManager entityManager, List<Long> roomsIdx, RoomStatus roomStatus);

    List<Room> findAvailableRooms(EntityManager em, LocalDateTime checkIn, LocalDateTime checkOut, RoomStatus roomStatus);

    boolean existsByRoomNumber(EntityManager em, String roomNumber);

    int countTotalRooms();

    int countByStatus(RoomStatus status);

    void updateBookingRoom(EntityManager em, Long bookingId, Long newRoomId);

    boolean isRoomInUse(EntityManager em, Long roomId);

    List<Room> getRoomsByOrderIdAndType(EntityManager em, long orderId, String bookingType);

    List<Room> getRoomsByBookingId(EntityManager em, long bookingId);

    List<Room> getAvailableRoomsByType(EntityManager em, String roomTypeId);

    boolean transferRooms(EntityManager em, long orderId, String bookingType, List<Long> oldRoomIds, List<Long> newRoomIds);


}

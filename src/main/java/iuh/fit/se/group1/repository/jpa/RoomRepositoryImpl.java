package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.repository.interfaces.RoomRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RoomRepositoryImpl extends AbstractRepositoryImpl<Room, Long> implements RoomRepository {
    public RoomRepositoryImpl() {
        super(Room.class);
    }

    @Override
    public List<Room> findByRoomNumberOrId(String keyword) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT r
                        FROM Room r
                        JOIN FETCH r.roomType rt
                        WHERE r.isDeleted = false
                          AND (
                                CAST(r.roomId AS string) LIKE :kw
                             OR LOWER(r.roomNumber) LIKE LOWER(:kw)
                          )
                        ORDER BY r.roomId ASC, r.roomNumber ASC
                    """;

            return em.createQuery(jpql, Room.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        });
    }

    @Override
    public List<Room> findRoomByStatusAndRoomType(String roomTypeId, RoomStatus roomStatus) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT r
                        FROM Room r
                        JOIN FETCH r.roomType rt
                        WHERE rt.roomTypeId = :typeId
                          AND r.roomStatus = :status
                          AND r.isDeleted = false
                        ORDER BY r.roomId ASC
                    """;

            return em.createQuery(jpql, Room.class)
                    .setParameter("typeId", roomTypeId)
                    .setParameter("status", roomStatus)
                    .getResultList();
        });
    }

    @Override
    public void updateRoomStatusBatch(List<Long> roomIds, RoomStatus roomStatus) {
        runInTransaction(em -> {

            em.createQuery("""
                                UPDATE Room r
                                SET r.roomStatus = :status
                                WHERE r.roomId IN :ids
                            """)
                    .setParameter("status", roomStatus)
                    .setParameter("ids", roomIds)
                    .executeUpdate();

        });
    }

    @Override
    public List<Room> findAvailableRooms(LocalDateTime checkIn,
                                         LocalDateTime checkOut,
                                         RoomStatus roomStatus) {

        return callInTransaction(em -> {

            String jpql = """
                        SELECT r
                        FROM Room r
                        WHERE r.isDeleted = false
                          AND r.roomStatus = :status
                          AND r.roomId NOT IN (
                                SELECT b.room.roomId
                                FROM Booking b
                                WHERE b.checkInDate < :checkOut
                                  AND b.checkOutDate > :checkIn
                          )
                        ORDER BY r.roomId ASC
                    """;

            return em.createQuery(jpql, Room.class)
                    .setParameter("status", roomStatus)
                    .setParameter("checkIn", checkIn)
                    .setParameter("checkOut", checkOut)
                    .getResultList();
        });
    }

    @Override
    public boolean existsByRoomNumber(String roomNumber) {
        return callInTransaction(em -> {

            return !em.createQuery("""
                                SELECT r.id
                                FROM Room r
                                WHERE r.roomNumber = :roomNumber
                                  AND r.isDeleted = false
                            """)
                    .setParameter("roomNumber", roomNumber)
                    .setMaxResults(1)
                    .getResultList()
                    .isEmpty();
        });
    }

    @Override
    public int countTotalRooms() {
        return 0;
    }

    @Override
    public int countByStatus(RoomStatus status) {
        return 0;
    }

    @Override
    public void updateBookingRoom(Long bookingId, Long newRoomId) {
        runInTransaction(em -> {

            em.createQuery("""
                                UPDATE Booking b
                                SET b.room.roomId = :roomId
                                WHERE b.bookingId = :bookingId
                            """)
                    .setParameter("roomId", newRoomId)
                    .setParameter("bookingId", bookingId)
                    .executeUpdate();
        });
    }

    @Override
    public boolean isRoomInUse(Long roomId) {
        return callInTransaction(em -> {
            String jpql = """
                        SELECT COUNT(b)
                        FROM Booking b
                        JOIN b.order o
                        JOIN o.orderType ot
                        WHERE b.room.roomId = :roomId
                          AND ot.id = 2
                    """;

            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("roomId", roomId)
                    .getSingleResult();

            return count > 0;
        });
    }

    @Override
    public List<Room> getRoomsByOrderIdAndType(long orderId, String bookingType) {
        return callInTransaction(em -> {
            String jpql = """
                        SELECT r
                        FROM Booking b
                        JOIN b.room r
                        JOIN FETCH r.roomType rt
                        WHERE b.order.orderId = :orderId
                          AND b.bookingType = :bookingType
                    """;

            return em.createQuery(jpql, Room.class)
                    .setParameter("orderId", orderId)
                    .setParameter("bookingType", BookingType.valueOf(bookingType))
                    .getResultList();
        });
    }

    @Override
    public List<Room> getRoomsByBookingId(long bookingId) {
        return callInTransaction(em -> {
            String jpql = """
                        SELECT r
                        FROM Booking b
                        JOIN b.room r
                        JOIN FETCH r.roomType
                        WHERE b.bookingId = :bookingId
                    """;

            return em.createQuery(jpql, Room.class)
                    .setParameter("bookingId", bookingId)
                    .getResultList();
        });
    }

    @Override
    public List<Room> getAvailableRoomsByType(String roomTypeId) {
        return callInTransaction(em -> {
            String jpql = """
                        SELECT r
                        FROM Room r
                        JOIN FETCH r.roomType rt
                        WHERE r.roomStatus = :status
                          AND rt.roomTypeId = :roomTypeId
                        ORDER BY r.roomNumber
                    """;

            return em.createQuery(jpql, Room.class)
                    .setParameter("status", RoomStatus.AVAILABLE)
                    .setParameter("roomTypeId", roomTypeId)
                    .getResultList();
        });
    }

    @Override
    public boolean transferRooms(long orderId, String bookingType,
                                 List<Long> oldRoomIds, List<Long> newRoomIds) {

        return callInTransaction(em -> {
            try {
                // 1. Lấy booking gốc
                Booking bookingInfo = em.createQuery("""
                                SELECT b FROM Booking b
                                WHERE b.order.orderId = :orderId
                                  AND b.bookingType = :bookingType
                                  AND b.room.roomId = :roomId
                                """, Booking.class)
                        .setParameter("orderId", orderId)
                        .setParameter("bookingType", BookingType.valueOf(bookingType))
                        .setParameter("roomId", oldRoomIds.get(0))
                        .setMaxResults(1)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);

                if (bookingInfo == null) return false;

                // 2. Update phòng cũ -> AVAILABLE
                em.createQuery("""
                                UPDATE Room r
                                SET r.roomStatus = :status
                                WHERE r.roomId IN :ids
                                """)
                        .setParameter("status", RoomStatus.AVAILABLE)
                        .setParameter("ids", oldRoomIds)
                        .executeUpdate();

                // 3. Xóa booking cũ
                em.createQuery("""
                                DELETE FROM Booking b
                                WHERE b.order.orderId = :orderId
                                  AND b.bookingType = :bookingType
                                  AND b.room.roomId IN :ids
                                """)
                        .setParameter("orderId", orderId)
                        .setParameter("bookingType", BookingType.valueOf(bookingType))
                        .setParameter("ids", oldRoomIds)
                        .executeUpdate();

                // 4. Insert booking mới
                Order orderRef = em.getReference(Order.class, orderId);

                for (Long roomId : newRoomIds) {
                    Room roomRef = em.getReference(Room.class, roomId);

                    Booking newBooking = new Booking();
                    newBooking.setOrder(orderRef);
                    newBooking.setRoom(roomRef);
                    newBooking.setCheckInDate(bookingInfo.getCheckInDate());
                    newBooking.setCheckOutDate(bookingInfo.getCheckOutDate());
                    newBooking.setBookingType(BookingType.valueOf(bookingType));
                    newBooking.setCreatedAt(LocalDate.now());

                    em.persist(newBooking);
                }

                // 5. Update phòng mới -> OCCUPIED
                em.createQuery("""
                                UPDATE Room r
                                SET r.roomStatus = :status
                                WHERE r.roomId IN :ids
                                """)
                        .setParameter("status", RoomStatus.OCCUPIED)
                        .setParameter("ids", newRoomIds)
                        .executeUpdate();

                return true;

            } catch (Exception e) {
                throw new RuntimeException("Error transferring rooms", e);
            }
        });
    }
}

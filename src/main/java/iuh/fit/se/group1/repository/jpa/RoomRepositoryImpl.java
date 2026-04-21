package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.repository.interfaces.RoomRepository;

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
}

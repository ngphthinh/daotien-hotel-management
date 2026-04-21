package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.repository.interfaces.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

public class BookingRepositoryImpl extends AbstractRepositoryImpl<Booking, Long> implements BookingRepository {
    public BookingRepositoryImpl() {
        super(Booking.class);
    }

    public boolean isExistsByRoomAndDate(Long roomId,
                                         LocalDateTime checkIn,
                                         LocalDateTime checkOut) {

        return callInTransaction(em ->

                !em.createQuery("""
                                    SELECT b.bookingId
                                    FROM Booking b
                                    WHERE b.room.roomId = :roomId
                                      AND b.checkInDate < :checkOut
                                      AND b.checkOutDate > :checkIn
                                """)
                        .setParameter("roomId", roomId)
                        .setParameter("checkIn", checkIn)
                        .setParameter("checkOut", checkOut)
                        .setMaxResults(1)
                        .getResultList()
                        .isEmpty()
        );
    }

    @Override
    public void saveAllBookingsForOrder(Order order, List<Booking> bookings) {
        runInTransaction(em -> {
            for (Booking b : bookings) {
                b.setOrder(order);
                em.persist(b);
            }
        });
    }


    @Override
    public List<BookingDisplayDTO> findAllBookingDisplay() {
        return callInTransaction(em ->
                em.createQuery("""
                                    SELECT new iuh.fit.se.group1.dto.BookingDisplayDTO(
                                        b.bookingId,
                                        r.roomNumber,
                                        c.fullName,
                                        c.phone
                                    )
                                    FROM Booking b
                                    JOIN b.room r
                                    JOIN b.order o
                                    JOIN o.customer c
                                    JOIN o.orderType ot
                                    WHERE ot.id = 2
                                """, BookingDisplayDTO.class)
                        .getResultList()
        );
    }

    @Override
    public void removeBookingsFromOrder(Order order, List<Booking> keepList) {
        runInTransaction(em -> {

            List<Long> keepIds = keepList.stream()
                    .map(Booking::getBookingId)
                    .toList();

            em.createQuery("""
                                DELETE FROM Booking b
                                WHERE b.order.orderId = :orderId
                                  AND b.bookingId NOT IN :ids
                            """)
                    .setParameter("orderId", order.getOrderId())
                    .setParameter("ids", keepIds.isEmpty() ? List.of(-1L) : keepIds)
                    .executeUpdate();

        });
    }

    @Override
    public void moveBookingsToOrder(Long targetOrderId, List<Long> bookingIds) {

        runInTransaction(em -> {

            em.createQuery("""
                                UPDATE Booking b
                                SET b.order.orderId = :orderId
                                WHERE b.bookingId IN :ids
                            """)
                    .setParameter("orderId", targetOrderId)
                    .setParameter("ids", bookingIds)
                    .executeUpdate();


        });
    }

    @Override
    public void updateBookingDates(Long bookingId, LocalDateTime checkIn, LocalDateTime checkOut) {
        runInTransaction(em -> {

            em.createQuery("""
                                UPDATE Booking b
                                SET b.checkInDate = :in,
                                    b.checkOutDate = :out
                                WHERE b.bookingId = :id
                            """)
                    .setParameter("in", checkIn)
                    .setParameter("out", checkOut)
                    .setParameter("id", bookingId)
                    .executeUpdate();

        });
    }

    @Override
    public void deleteByOrderId(Long id) {
        runInTransaction(em -> {

            em.createQuery("""
                                DELETE FROM Booking b
                                WHERE b.order.orderId = :id
                            """)
                    .setParameter("id", id)
                    .executeUpdate();

        });
    }

    @Override
    public List<Booking> findByOrderId(Long orderId) {
        return callInTransaction(em ->

                em.createQuery("""
                                    SELECT b
                                    FROM Booking b
                                    JOIN FETCH b.room r
                                    JOIN FETCH r.roomType
                                    WHERE b.order.orderId = :id
                                """, Booking.class)
                        .setParameter("id", orderId)
                        .getResultList()
        );
    }

    @Override
    public int countRoomsNearExpiry(LocalDateTime from, LocalDateTime to) {
        return callInTransaction(em ->

                em.createQuery("""
                                    SELECT COUNT(DISTINCT b.room.roomId)
                                    FROM Booking b
                                    WHERE b.checkOutDate BETWEEN :from AND :to
                                      AND b.checkOutDate IS NOT NULL
                                """, Long.class)
                        .setParameter("from", from)
                        .setParameter("to", to)
                        .getSingleResult()
                        .intValue()
        );
    }

    @Override
    public int countCheckIns(LocalDateTime start, LocalDateTime end) {
        return callInTransaction(em ->

                em.createQuery("""
                                    SELECT COUNT(b)
                                    FROM Booking b
                                    WHERE b.checkInDate BETWEEN :start AND :end
                                """, Long.class)
                        .setParameter("start", start)
                        .setParameter("end", end)
                        .getSingleResult()
                        .intValue()
        );
    }

    @Override
    public int countCheckOuts(LocalDateTime start, LocalDateTime end) {
        return callInTransaction(em ->

                em.createQuery("""
                                    SELECT COUNT(b)
                                    FROM Booking b
                                    WHERE b.checkOutDate BETWEEN :start AND :end
                                      AND b.checkOutDate IS NOT NULL
                                """, Long.class)
                        .setParameter("start", start)
                        .setParameter("end", end)
                        .getSingleResult()
                        .intValue()
        );
    }

    @Override
    public int countCheckedOutRooms(LocalDateTime startDate, LocalDateTime endDate) {
        return 0;
    }

    @Override
    public int countLateCheckOuts(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime deadlineTime) {
        return 0;
    }
}

package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.dto.BookingDTO;
import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.repository.interfaces.BookingRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepositoryImpl extends AbstractRepositoryImpl<Booking, Long> implements BookingRepository {
    public BookingRepositoryImpl() {
        super(Booking.class);
    }

    public boolean isExistsByRoomAndDate(EntityManager em, Long roomId,
                                         LocalDateTime checkIn,
                                         LocalDateTime checkOut) {

        return

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
                ;
    }

    @Override
    public void saveAllBookingsForOrder(EntityManager em, Order order, List<Booking> bookings) {

        for (Booking b : bookings) {
            b.setOrder(order);
            em.persist(b);
        }

    }


    @Override
    public List<BookingDisplayDTO> findAllBookingDisplay(EntityManager em) {
        return
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
                        .getResultList();
    }

    @Override
    public List<BookingDTO> getAllBookings(EntityManager em) {
        String sql = """
                    SELECT
                        MIN(b.bookingId) as bookingId,
                        c.fullName,
                        c.citizenId,
                        b.bookingType,
                        MIN(o.orderId) as orderId,
                        MIN(ot.name) as orderTypeName,
                        STRING_AGG(r.roomNumber, ', ') as rooms
                    FROM Booking b
                    JOIN Orders o ON b.orderId = o.orderId
                    JOIN Customer c ON o.customerId = c.customerId
                    JOIN Room r ON b.roomId = r.roomId
                    JOIN OrderType ot ON o.orderTypeId = ot.orderTypeId
                    WHERE o.orderTypeId IN (2, 3)
                    AND (
                        (b.checkInDate <= GETDATE() AND b.checkOutDate >= GETDATE())
                        OR
                        (CAST(b.checkInDate AS DATE) = CAST(GETDATE() AS DATE)
                         AND b.checkInDate > GETDATE())
                    )
                    GROUP BY c.citizenId, c.fullName, b.bookingType, o.orderId
                    ORDER BY c.citizenId, b.bookingType
                """;
        List<Object[]> rows = em.createNativeQuery(sql, Object[].class).getResultList();
        List<BookingDTO> result = new ArrayList<>();


        for (Object[] r : rows) {
            BookingDTO dto = new BookingDTO();
            dto.bookingId = ((Number) r[0]).longValue();
            dto.bookingIdDisplay = String.valueOf(dto.bookingId);
            dto.guestName = (String) r[1];
            dto.cccd = (String) r[2];
            dto.bookingType = BookingType.valueOf((String) r[3]);
            dto.orderId = ((Number) r[4]).longValue();
            dto.orderTypeName = (String) r[5];
            dto.rooms = (String) r[6];
            result.add(dto);
        }
        return result;
    }


    @Override
    public Booking getBookingById(EntityManager em, long bookingId, long roomId) {
        return
                em.createQuery("""
                                    SELECT b FROM Booking b
                                    WHERE b.bookingId = :bookingId
                                      AND b.room.roomId = :roomId
                                """, Booking.class)
                        .setParameter("bookingId", bookingId)
                        .setParameter("roomId", roomId)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
    }

    @Override
    public boolean extendRoomBooking(EntityManager em, Long orderId, List<Long> roomIds,
                                     int extendValue, String bookingType) {


        if (BookingType.OVERNIGHT.name().equalsIgnoreCase(bookingType)) {
            return false;
        }

        // 1. Lấy checkout hiện tại (lấy 1 phòng đại diện)
        LocalDateTime currentCheckOut = em.createQuery("""
                        SELECT b.checkOutDate
                        FROM Booking b
                        WHERE b.order.orderId = :orderId
                          AND b.bookingType = :bookingType
                          AND b.room.roomId = :roomId
                        """, LocalDateTime.class)
                .setParameter("orderId", orderId)
                .setParameter("bookingType", BookingType.valueOf(bookingType))
                .setParameter("roomId", roomIds.get(0))
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (currentCheckOut == null) {
            return false;
        }

        // 2. Tính checkout mới
        LocalDateTime newCheckOut;
        if ("HOURLY".equals(bookingType)) {
            newCheckOut = currentCheckOut.plusHours(extendValue);
        } else if ("DAILY".equals(bookingType)) {
            newCheckOut = currentCheckOut.plusDays(extendValue);
        } else {
            return false;
        }

        // 3. Update tất cả room
        int updated = em.createQuery("""
                        UPDATE Booking b
                        SET b.checkOutDate = :newCheckOut
                        WHERE b.order.orderId = :orderId
                          AND b.bookingType = :bookingType
                          AND b.room.roomId IN :roomIds
                        """)
                .setParameter("newCheckOut", newCheckOut)
                .setParameter("orderId", orderId)
                .setParameter("bookingType", BookingType.valueOf(bookingType))
                .setParameter("roomIds", roomIds)
                .executeUpdate();

        return updated > 0;

    }

    @Override
    public boolean cancelRoomBooking(EntityManager em, Long orderId, Long roomId, String bookingType) {


        Booking booking = em.createQuery("""
                                SELECT b FROM Booking b
                                WHERE b.order.orderId = :orderId
                                  AND b.bookingType = :bookingType
                                  AND b.room.roomId = :roomId
                        """, Booking.class)
                .setParameter("orderId", orderId)
                .setParameter("bookingType", BookingType.valueOf(bookingType))
                .setParameter("roomId", roomId)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (booking == null) return false;

        // đã check-in -> không cho hủy
        if (booking.getCheckInDate() != null &&
                booking.getCheckInDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        em.remove(booking);

        // nếu cần:
        // Room room = booking.getRoom();
        // room.setRoomStatus(RoomStatus.AVAILABLE);

        return true;

    }

    @Override
    public Booking getBookingByOrderIdAndType(EntityManager em, long orderId, String bookingType, long roomId) {
        return
                em.createQuery("""
                                    SELECT b FROM Booking b
                                    WHERE b.order.orderId = :orderId
                                      AND b.bookingType = :bookingType
                                      AND b.room.roomId = :roomId
                                """, Booking.class)
                        .setParameter("orderId", orderId)
                        .setParameter("bookingType", BookingType.valueOf(bookingType))
                        .setParameter("roomId", roomId)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
    }

    @Override
    public List<BookingDTO> searchBookingsByCitizenId(EntityManager em, String citizenId) {

        String sql = """
                    SELECT
                        MIN(b.bookingId), c.fullName, c.citizenId,
                        b.bookingType, MIN(o.orderId),
                        MIN(ot.name),
                        STRING_AGG(r.roomNumber, ', ')
                    FROM Booking b
                    JOIN Orders o ON b.orderId = o.orderId
                    JOIN Customer c ON o.customerId = c.customerId
                    JOIN Room r ON b.roomId = r.roomId
                    JOIN OrderType ot ON o.orderTypeId = ot.orderTypeId
                    WHERE o.orderTypeId IN (2, 3)
                      AND (
                        (b.checkInDate <= GETDATE() AND b.checkOutDate >= GETDATE())
                        OR
                        (CAST(b.checkInDate AS DATE) = CAST(GETDATE() AS DATE)
                         AND b.checkInDate > GETDATE())
                      )
                      AND c.citizenId LIKE ?
                    GROUP BY c.citizenId, c.fullName, b.bookingType, o.orderId
                """;

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter(1, "%" + citizenId + "%")
                .getResultList();

        List<BookingDTO> result = new ArrayList<>();

        for (Object[] r : rows) {
            BookingDTO dto = new BookingDTO();
            dto.bookingId = ((Number) r[0]).longValue();
            dto.bookingIdDisplay = String.valueOf(dto.bookingId);
            dto.guestName = (String) r[1];
            dto.cccd = (String) r[2];
            dto.bookingType = BookingType.valueOf((String) r[3]);
            dto.orderId = ((Number) r[4]).longValue();
            dto.orderTypeName = (String) r[5];
            dto.rooms = (String) r[6];
            result.add(dto);
        }
        return result;

    }

    @Override
    public void removeBookingsFromOrder(EntityManager em, Order order, List<Booking> keepList) {


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


    }

    @Override
    public void moveBookingsToOrder(EntityManager em, Long targetOrderId, List<Long> bookingIds) {


        em.createQuery("""
                            UPDATE Booking b
                            SET b.order.orderId = :orderId
                            WHERE b.bookingId IN :ids
                        """)
                .setParameter("orderId", targetOrderId)
                .setParameter("ids", bookingIds)
                .executeUpdate();


    }

    @Override
    public void updateBookingDates(EntityManager em, Long bookingId, LocalDateTime checkIn, LocalDateTime checkOut) {


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


    }

    @Override
    public void deleteByOrderId(EntityManager em, Long id) {


        em.createQuery("""
                            DELETE FROM Booking b
                            WHERE b.order.orderId = :id
                        """)
                .setParameter("id", id)
                .executeUpdate();


    }

    @Override
    public List<Booking> findByOrderId(EntityManager em, Long orderId) {


        return em.createQuery("""
                            SELECT b
                            FROM Booking b
                            JOIN FETCH b.room r
                            JOIN FETCH r.roomType
                            WHERE b.order.orderId = :id
                        """, Booking.class)
                .setParameter("id", orderId)
                .getResultList();

    }

    @Override
    public int countRoomsNearExpiry(EntityManager em, LocalDateTime from, LocalDateTime to) {

        return
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
                ;
    }

    @Override
    public int countCheckIns(EntityManager em, LocalDateTime start, LocalDateTime end) {
        return

                em.createQuery("""
                                    SELECT COUNT(b)
                                    FROM Booking b
                                    WHERE b.checkInDate BETWEEN :start AND :end
                                """, Long.class)
                        .setParameter("start", start)
                        .setParameter("end", end)
                        .getSingleResult()
                        .intValue()
                ;
    }

    @Override
    public int countCheckOuts(EntityManager em, LocalDateTime start, LocalDateTime end) {
        return

                em.createQuery("""
                                    SELECT COUNT(b)
                                    FROM Booking b
                                    WHERE b.checkOutDate BETWEEN :start AND :end
                                      AND b.checkOutDate IS NOT NULL
                                """, Long.class)
                        .setParameter("start", start)
                        .setParameter("end", end)
                        .getSingleResult()
                        .intValue();
    }

    @Override
    public int countCheckedOutRooms(EntityManager em, LocalDateTime startDate, LocalDateTime endDate) {
        return 0;
    }

    @Override
    public int countLateCheckOuts(EntityManager em, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime deadlineTime) {
        return 0;
    }
}

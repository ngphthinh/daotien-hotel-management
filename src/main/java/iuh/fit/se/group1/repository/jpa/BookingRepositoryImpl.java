package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.dto.BookingDTO;
import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.repository.interfaces.BookingRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<BookingDTO> getAllBookings() {
        return callInTransaction(em -> {
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

            List<Object[]> rows = em.createNativeQuery(sql).getResultList();
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
        });
    }

    @Override
    public Booking getBookingById(long bookingId, long roomId) {
        return callInTransaction(em ->
                em.createQuery("""
                                    SELECT b FROM Booking b
                                    WHERE b.bookingId = :bookingId
                                      AND b.room.roomId = :roomId
                                """, Booking.class)
                        .setParameter("bookingId", bookingId)
                        .setParameter("roomId", roomId)
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
        );
    }

    @Override
    public boolean extendRoomBooking(Long orderId, List<Long> roomIds,
                                     int extendValue, String bookingType) {

        return callInTransaction(em -> {

            if ("OVERNIGHT".equals(bookingType)) {
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
        });
    }

    @Override
    public boolean cancelRoomBooking(Long orderId, Long roomId, String bookingType) {
        return callInTransaction(em -> {

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
        });
    }

    @Override
    public Booking getBookingByOrderIdAndType(long orderId, String bookingType, long roomId) {
        return callInTransaction(em ->
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
                        .orElse(null)
        );
    }

    @Override
    public List<BookingDTO> searchBookingsByCitizenId(String citizenId) {
        return callInTransaction(em -> {
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
        });
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

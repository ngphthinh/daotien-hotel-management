package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.BookingDTO;
import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.repository.jpa.BookingRepositoryImpl;
import jakarta.persistence.EntityManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingService extends Service {
    private final BookingRepositoryImpl bookingRepositoryImpl;

    public BookingService() {
        this.bookingRepositoryImpl = new BookingRepositoryImpl();
    }

    public boolean existsByRoomIdAndDate(Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
//        return bookingRepositoryImpl.isExistsByRoomAndDate(roomId, checkInDate, checkOutDate);
        return doInTransaction(entityManager -> bookingRepositoryImpl.isExistsByRoomAndDate(entityManager, roomId, checkInDate, checkOutDate));
    }


    public double getPriceFromBooking(Booking booking) {

        int timePlus = (int) calculateBookingDuration(booking.getCheckInDate(), booking.getCheckOutDate(), booking.getBookingType());

        RoomType roomType = booking.getRoom().getRoomType();

        if (booking.getBookingType().equals(BookingType.HOURLY)) { // Theo giờ
            return roomType.getHourlyRate().longValueExact() + ((timePlus - 1) * roomType.getAdditionalHourRate().longValueExact());
        } else if (booking.getBookingType().equals(BookingType.DAILY)) { // Theo ngày
            return roomType.getDailyRate().longValueExact() * timePlus;
        } else if (booking.getBookingType().equals(BookingType.OVERNIGHT)) { // qua đêm
            return roomType.getOvernightRate().longValueExact();
        }
        return 0;
    }


    private long calculateBookingDuration(LocalDateTime checkIn, LocalDateTime checkOut, BookingType bookingType) {
        switch (bookingType) {
            case HOURLY:
                // Tính số giờ, làm tròn lên
                long hours = Duration.between(checkIn, checkOut).toHours();
                long minutes = Duration.between(checkIn, checkOut).toMinutes() % 60;
                return minutes > 0 ? hours + 1 : hours;

            case DAILY:
                // Tính số ngày thuê: mốc 12h trưa
                // Check-out trước 12h -> không tính ngày đó
                // Check-out từ 12h trở đi -> tính ngày đó
                // VD: 08/12 14:00 -> 12/12 12:00 = 3 ngày
                // 08/12 14:00 -> 12/12 14:00 = 4 ngày
                long days = ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());

                return Math.max(1, days);

            case OVERNIGHT:
                return 1;

            default:
                return 1;
        }
    }


    public List<Booking> getBookingsByOrderId(EntityManager em, Long orderId) {

//        return bookingRepositoryImpl.findByOrderId(orderId);
        return bookingRepositoryImpl.findByOrderId(em, orderId);
    }


    public List<BookingDTO> getAllBookings() {
//        return bookingRepositoryImpl.getAllBookings();
        return doInTransaction(bookingRepositoryImpl::getAllBookings);
    }


    public Booking getBookingById(long bookingId, long roomId) {
//        return bookingRepositoryImpl.getBookingById(bookingId, roomId);
        return doInTransaction(entityManager -> bookingRepositoryImpl.getBookingById(entityManager, bookingId, roomId));
    }


    public boolean extendRoomBooking(Long orderId, List<Long> roomIds, int extendValue, String bookingType) {

//        return bookingRepositoryImpl.extendRoomBooking(orderId, roomIds, extendValue, bookingType);
        return doInTransaction(entityManager -> bookingRepositoryImpl.extendRoomBooking(entityManager, orderId, roomIds, extendValue, bookingType));
    }


    public boolean cancelRoomBooking(Long orderId, Long roomId, String bookingType) {
//        return bookingRepositoryImpl.cancelRoomBooking(orderId, roomId, bookingType);
        return doInTransaction(entityManager -> bookingRepositoryImpl.cancelRoomBooking(entityManager, orderId, roomId, bookingType));
    }


    public Booking getBookingByOrderIdAndType(long orderId, String bookingType, long roomId) {
//        return bookingRepositoryImpl.getBookingByOrderIdAndType(orderId, bookingType, roomId);
//        return bookingRepositoryImpl.getBookingByOrderIdAndType(entityManager, orderId, bookingType, roomId);
        return doInTransaction(em -> bookingRepositoryImpl.getBookingByOrderIdAndType(em, orderId, bookingType, roomId));

    }

    public List<BookingDTO> searchBookingsByCitizenId(String citizenId) {
//        return bookingRepositoryImpl.searchBookingsByCitizenId(citizenId);
        return doInTransaction(entityManager -> bookingRepositoryImpl.searchBookingsByCitizenId(entityManager, citizenId));

    }
}

package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.repository.jpa.BookingRepositoryImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingService {
    private final BookingRepositoryImpl bookingRepositoryImpl;

    public BookingService() {
        this.bookingRepositoryImpl = new BookingRepositoryImpl();
    }

    public boolean existsByRoomIdAndDate(Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        return bookingRepositoryImpl.isExistsByRoomAndDate(roomId, checkInDate, checkOutDate);
    }

    public List<Booking> getAllBookings() {
        return bookingRepositoryImpl.findAll();
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


    public List<Booking> getBookingsByOrderId(Long orderId) {
        return bookingRepositoryImpl.findByOrderId(orderId);
    }
}

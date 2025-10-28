package iuh.fit.se.group1.service;

import iuh.fit.se.group1.repository.BookingRepository;

import java.time.LocalDateTime;

public class BookingService {
    private final BookingRepository bookingRepository;
    public BookingService() {
        this.bookingRepository = new BookingRepository();
    }

    public boolean existsByRoomIdAndDate(Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        return bookingRepository.isExistsByRoomAndDate(roomId, checkInDate, checkOutDate);
    }




}

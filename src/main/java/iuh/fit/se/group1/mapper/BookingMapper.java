package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.BookingDTO;
import iuh.fit.se.group1.dto.BookingViewDTO;
import iuh.fit.se.group1.dto.OrderDTO;
import iuh.fit.se.group1.entity.Booking;
import iuh.fit.se.group1.entity.Order;

public class BookingMapper {

    private final RoomMapper roomMapper;

    public BookingMapper() {
        this.roomMapper = new RoomMapper();
    }

    public BookingViewDTO toDTO(Booking booking) {
        if (booking == null) return null;

        return BookingViewDTO.builder()
                .bookingId(booking.getBookingId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .bookingType(booking.getBookingType())
                .room(roomMapper.toRoomDTO(booking.getRoom()))
                // chỉ lấy orderId, KHÔNG map full
                .orderId(booking.getOrder() != null ? booking.getOrder().getOrderId() : null)
                .build();
    }

    public Booking toBooking(BookingViewDTO dto) {
        if (dto == null) return null;

        return Booking.builder()
                .bookingId(dto.getBookingId())
                .checkInDate(dto.getCheckInDate())
                .checkOutDate(dto.getCheckOutDate())
                .bookingType(dto.getBookingType())
                .room(roomMapper.toRoom(dto.getRoom()))
                // chỉ set id
                .order(dto.getOrderId() != null ? Order.builder().orderId(dto.getOrderId()).build() : null)
                .build();
    }


}

package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.BookingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"order", "room"})
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    @Enumerated
    private BookingType bookingType;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    @OneToOne
    @JoinColumn(name = "roomId")
    private Room room;
    private LocalDate createdAt;


    public Booking(Order order) {
        this.order = order;
    }


    public Booking(LocalDateTime checkInDate, LocalDateTime checkOutDate, Room room, BookingType bookingType, Order order) {
        this.order = order;
        this.setCheckInDate(checkInDate);
        this.setCheckOutDate(checkOutDate);
        this.setRoom(room);
        this.setBookingType(bookingType);
        this.setCreatedAt(LocalDate.now());
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Booking booking)) return false;
        return Objects.equals(bookingId, booking.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookingId);
    }


}

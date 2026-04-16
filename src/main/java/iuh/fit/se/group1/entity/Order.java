package iuh.fit.se.group1.entity;


import iuh.fit.se.group1.enums.PaymentType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"employee", "employeePayment", "orderType", "customer", "promotion", "bookings"})
@EqualsAndHashCode(of = "orderId")
@Entity
public class Order {
    @Id
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employeeId")
    private Employee employee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderTypeId")
    private OrderType orderType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "promotionId")
    private Promotion promotion;

    private BigDecimal deposit;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "order")
    private List<Booking> bookings;

    private LocalDate paymentDate;
    @Enumerated
    private PaymentType paymentType;



    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employeePaymentId")
    private Employee employeePayment;
    private boolean isDelete;

    public Order() {
        bookings = new ArrayList<>();
    }

    public void addBooking(Booking booking) {
        booking.setOrder(this);
        this.bookings.add(booking);
    }

}



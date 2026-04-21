package iuh.fit.se.group1.entity;


import iuh.fit.se.group1.enums.PaymentType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


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
@Table(name = "Orders")
@Entity
@SQLDelete(sql = "UPDATE Orders SET isDeleted = true WHERE orderId = ?")
@SQLRestriction("isDeleted = false")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;



    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employeePaymentId")
    private Employee employeePayment;
    private boolean isDeleted;

    public Order() {
        bookings = new ArrayList<>();
    }

    public void addBooking(Booking booking) {
        booking.setOrder(this);
        this.bookings.add(booking);
    }

}



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
@Builder
@SQLDelete(sql = "UPDATE Orders SET isDeleted = 1 WHERE orderId = ?")
@SQLRestriction("isDeleted = 0")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "orderTypeId")
    private OrderType orderType;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "promotionId")
    private Promotion promotion;

    private BigDecimal deposit;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<Booking> bookings;


    private LocalDate paymentDate;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;


    @ManyToOne
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



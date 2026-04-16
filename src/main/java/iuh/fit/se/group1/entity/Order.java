package iuh.fit.se.group1.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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
    private Promotion promotion;
    private BigDecimal deposit;
    private LocalDate createdAt;
    private List<Booking> bookings;
    private LocalDate paymentDate;
    private Employee employeePayment;
    private boolean isDelete;


}

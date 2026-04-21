package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@IdClass(OrderDetail.OrderDetailId.class)
public class OrderDetail {
    private BigDecimal unitPrice;

    @Id
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;
    @Id
    @ManyToOne
    @JoinColumn(name = "amenityId")
    private Amenity amenity;
    private int quantity;
    private LocalDate createdAt;


    public OrderDetail(BigDecimal unitPrice, Amenity amenity, int quantity, LocalDate createdAt) {
        this.unitPrice = unitPrice;
        this.amenity = amenity;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public OrderDetail(BigDecimal unitPrice, Amenity amenity, int quantity) {
        this.unitPrice = unitPrice;
        this.amenity = amenity;
        this.quantity = quantity;
        this.createdAt = LocalDate.now();
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class OrderDetailId implements Serializable {
        private Long order;
        private String amenity;

    }

}

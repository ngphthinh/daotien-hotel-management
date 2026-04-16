package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@IdClass(SurchargeDetail.SurchargeDetailID.class)
public class SurchargeDetail {


    @Id
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "surchargerId")
    private Surcharge surcharge;
    private int quantity;
    private LocalDate createdAt;


    public SurchargeDetail(Surcharge surcharge, int quantity, LocalDate createdAt) {
        this.surcharge = surcharge;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public SurchargeDetail(Surcharge surcharge, int quantity) {
        this.surcharge = surcharge;
        this.quantity = quantity;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @Getter
    @Setter
    @ToString
    @Builder
    public static class SurchargeDetailID implements Serializable {
        private Long order;
        private Long surcharge;
    }


}

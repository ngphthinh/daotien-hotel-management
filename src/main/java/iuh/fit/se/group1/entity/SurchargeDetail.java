package iuh.fit.se.group1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class SurchargeDetail {


    @Id
    @ManyToOne
    @JoinColumn(name="orderId")
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


}

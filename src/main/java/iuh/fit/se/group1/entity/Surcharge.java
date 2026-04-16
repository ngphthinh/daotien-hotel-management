package iuh.fit.se.group1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"surchargeDetails"})
@Entity
public class Surcharge {
    @Id
    private Long surchargeId;
    private String name;
    private boolean isDelete;
    private BigDecimal price;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "Surcharge")
    private Set<SurchargeDetail> surchargeDetails;


    public Surcharge(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
    
    public Surcharge(long surchargeId, String name, BigDecimal price) {
        this.surchargeId = surchargeId;
        this.name = name;
        this.price = price;
    }


    public Surcharge(long surchargeId) {
        this.surchargeId = surchargeId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(surchargeId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Surcharge surcharge = (Surcharge) o;
        return Objects.equals(surchargeId, surcharge.surchargeId);
    }
}

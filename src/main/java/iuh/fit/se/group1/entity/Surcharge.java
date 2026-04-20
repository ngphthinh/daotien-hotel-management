package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
@Builder
@SQLDelete(sql = "UPDATE Surcharge SET isDeleted = true WHERE surchargeId = ?")
@SQLRestriction("isDeleted = false")
public class Surcharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long surchargeId;
    @Column(columnDefinition = "nvarchar(255)")
    private String name;
    private boolean isDeleted;
    private BigDecimal price;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "surcharge")
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

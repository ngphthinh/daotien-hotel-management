package iuh.fit.se.group1.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Setter;
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
@ToString(exclude = {"orders"})
@Entity
@Builder
@SQLDelete(sql = "UPDATE Promotion SET isDeleted = true WHERE promotionId = ?")
@SQLRestriction("isDeleted = false")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long promotionId;
    @Column(columnDefinition = "nvarchar(255)")
    private String promotionName;
    @Column(columnDefinition = "nvarchar(1000)")
    private String description;
    private Float discountPercent;
    private BigDecimal minOrderAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate createdAt;
    @OneToMany(mappedBy = "promotion")
    private Set<Order> orders;

    private boolean isDeleted;

    public Promotion(String promotionName, String description, Float discountPercent, BigDecimal minOrderAmount, LocalDate startDate, LocalDate endDate, LocalDate createdAt) {
        this.promotionName = promotionName;
        this.description = description;
        this.discountPercent = discountPercent;
        this.minOrderAmount = minOrderAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
    }




    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Promotion promotion)) return false;
        return Objects.equals(promotionId, promotion.promotionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(promotionId);
    }

}

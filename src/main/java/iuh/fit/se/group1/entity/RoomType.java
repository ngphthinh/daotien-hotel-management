package iuh.fit.se.group1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"rooms"})
@Entity
@EqualsAndHashCode(of = "roomTypeId")
public class RoomType {
    @Id
    @Column(columnDefinition = "varchar(20)")
    private String roomTypeId;
    @Column(columnDefinition = "nvarchar(50)")
    private String name;
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    private BigDecimal overnightRate;
    private BigDecimal additionalHourRate;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "roomType")
    private Set<Room> rooms;
    public RoomType(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
}

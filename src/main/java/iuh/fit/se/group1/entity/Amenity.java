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
@ToString(exclude = {"orderDetails"})
@Entity
@Builder
@SQLDelete(sql = "UPDATE Amenity SET isDeleted = true WHERE amenityId = ?")
@SQLRestriction("isDeleted = false")
public class Amenity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long amenityId;
    private String nameAmenity;
    private BigDecimal price;
	private boolean isDeleted;
    private LocalDate createdAt;

	@OneToMany(mappedBy = "amenity")
	private Set<OrderDetail> orderDetails;


	public Amenity(Long amenityId, String nameAmenity, BigDecimal price) {
		this.amenityId = amenityId;
		this.nameAmenity = nameAmenity;
		this.price = price;
	}

	public Amenity(Long amenityId) {
		this.amenityId = amenityId;
	}

	public Amenity(String nameAmenity, BigDecimal price) {
		this.nameAmenity = nameAmenity;
		this.price = price;
	}


	@Override
	public int hashCode() {
		return Objects.hash(amenityId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Amenity other = (Amenity) obj;
		return Objects.equals(amenityId, other.amenityId);
	}

    
}

package iuh.fit.se.group1.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AmenityDTO {
    private Long amenityId;
    private String nameAmenity;
    private BigDecimal price;
    private int quantity;

    @Override
    public String toString() {
        return nameAmenity;
    }
}

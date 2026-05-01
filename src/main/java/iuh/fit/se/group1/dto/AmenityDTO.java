package iuh.fit.se.group1.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AmenityDTO implements Serializable {
    private Long amenityId;
    private String nameAmenity;
    private BigDecimal price;
    private int quantity;
    private LocalDate createdAt;


    @Override
    public String toString() {
        return nameAmenity;
    }
}

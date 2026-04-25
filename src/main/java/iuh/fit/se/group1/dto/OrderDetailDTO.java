package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.entity.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetailDTO {
    private BigDecimal unitPrice;

    private AmenityDTO amenity;
    private int quantity;
    private LocalDate createdAt;
}

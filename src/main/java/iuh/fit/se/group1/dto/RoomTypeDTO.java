package iuh.fit.se.group1.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomTypeDTO {
    private String roomTypeId;
    private String name;
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    private BigDecimal overnightRate;
    private BigDecimal additionalHourRate;
}

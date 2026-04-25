package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SurchargeDTO {
    private Long surchargeId;
    private String name;
    private BigDecimal price;
    private int quantity;

    @Override
    public String toString() {
        return name;
    }
}

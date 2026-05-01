package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SurchargeDTO implements Serializable {
    private Long surchargeId;
    private String name;
    private BigDecimal price;
    private int quantity;
    private LocalDate createdAt;

    @Override
    public String toString() {
        return name;
    }
}

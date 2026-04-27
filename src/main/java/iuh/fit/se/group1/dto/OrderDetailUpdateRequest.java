package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetailUpdateRequest implements Serializable {
    private Long amenityId;
    private BigDecimal unitPrice;
    private int quantity;
    private Long orderId;
}

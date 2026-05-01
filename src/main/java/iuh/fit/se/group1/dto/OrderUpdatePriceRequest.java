package iuh.fit.se.group1.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderUpdatePriceRequest implements Serializable {
    private Long orderId;
    private BigDecimal totalPrice;
}

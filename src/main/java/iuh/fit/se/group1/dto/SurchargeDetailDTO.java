package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SurchargeDetailDTO {

    private OrderDTO order;

    private SurchargeDTO surcharge;
    private int quantity;
}

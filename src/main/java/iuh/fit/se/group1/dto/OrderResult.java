package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.SurchargeDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResult {
    private OrderDTO order;
    private SurchargeDetailDTO surchargeDetail;
}

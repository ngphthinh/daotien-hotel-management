package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SurchargeDetailUpdateRequest implements Serializable {
    private Long surchargeId;
    private int quantity;
    private Long orderId;
}

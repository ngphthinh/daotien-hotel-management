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
public class SurchargeDetailCreateRequest implements Serializable {
    private SurchargeDetailDTO newSurcharge;
    private Long orderId;

}

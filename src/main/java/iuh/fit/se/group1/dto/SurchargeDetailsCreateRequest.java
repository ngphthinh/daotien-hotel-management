package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SurchargeDetailsCreateRequest implements Serializable {
    private Long orderId;
    private List<SurchargeDetailDTO> surcharges;
}

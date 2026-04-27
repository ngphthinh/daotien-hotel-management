package iuh.fit.se.group1.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderUpdateOrderTypeRequest implements Serializable {
    private Long orderId;
    private Long orderTypeID;

}

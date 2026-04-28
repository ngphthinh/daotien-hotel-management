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
public class ExportOrderToPdfRequest implements Serializable {
    private Long order;
    private String promotionStr;
    private String paymentType;
    private String totalPricePayment;
    private String employeeCurrentFullName;


}

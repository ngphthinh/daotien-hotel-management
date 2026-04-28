package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.PaymentType;
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
public class PaymentRequest implements Serializable {
    private Long orderId;
    private PaymentType paymentType;
    private BigDecimal totalAmount;
    private Long employeePaymentId;
    private LocalDate paymentDate;
}

package iuh.fit.se.group1.dto;

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
public class PaymentResponse implements Serializable {
    private Long orderId;
    private String message;
    private String paymentStatus;
    private BigDecimal totalAmount;
    private LocalDate paymentDate;
    private Long employeePaymentId;
}

package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private EmployeeDTO employee;
    private OrderTypeDTO orderType;
    private CustomerDTO customer;
    private PromotionDTO promotion;
    private BigDecimal deposit;
    private LocalDate createdAt;
    private List<BookingViewDTO> bookings;
    private LocalDate paymentDate;
    private PaymentType paymentType;
    private EmployeeDTO employeePayment;
}

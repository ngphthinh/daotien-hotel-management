package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.enums.OrderBookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderTypeDTO {
    private Long orderTypeId;
    private OrderBookStatus name;
}

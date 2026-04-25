/**
 * @ (#) ShiftClose.java   1.0     31/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet
 * @version: 1.0
 * @created: 31/10/2025
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Builder
public class ShiftClose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shiftCloseId;
    @ManyToOne
    @JoinColumn(name = "employeeShiftId")
    private EmployeeShift employeeShift;
    private BigDecimal totalRevenue;
    private BigDecimal cashInDrawer;
    private BigDecimal difference;
    @Column(columnDefinition = "nvarchar(255)")
    private String note;
    @ManyToOne
    @JoinColumn(name = "managerId")
    private Employee manager;
    private LocalDateTime createdAt;

}

/**
 * @ (#) ShiftClose.java   1.0     31/10/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class ShiftClose {
    @Id
    private Long shiftCloseId;
    @ManyToOne
    @JoinColumn(name = "employeeShiftId")
    private EmployeeShift employeeShift;
    private BigDecimal totalRevenue;
    private  BigDecimal cashInDrawer;
    private BigDecimal difference;
    @Column(columnDefinition = "nvarchar(255)")
    private String note;
    private Long managerId;
    private LocalDateTime createdAt;

}

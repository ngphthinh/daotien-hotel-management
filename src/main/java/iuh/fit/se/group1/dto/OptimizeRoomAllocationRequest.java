package iuh.fit.se.group1.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizeRoomAllocationRequest implements Serializable {
    private int singleQuantity;
    private int doubleQuantity;
    private int adults;
    private int children;
}
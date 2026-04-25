package iuh.fit.se.group1.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShiftDTO {
    private Long shiftId;
    private String name;
    private String startTime;
    private String endTime;
}

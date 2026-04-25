package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO cho ghi chú ca làm việc từ ShiftClose
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ShiftNoteDto {
    private String employeeName;
    private String shiftName;
    private LocalDateTime shiftDate;
    private String note;
}


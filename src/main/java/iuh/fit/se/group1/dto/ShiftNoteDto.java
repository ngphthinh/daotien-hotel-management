package iuh.fit.se.group1.dto;

import java.time.LocalDateTime;

/**
 * DTO cho ghi chú ca làm việc từ ShiftClose
 */
public class ShiftNoteDto {
    private String employeeName;
    private String shiftName;
    private LocalDateTime shiftDate;
    private String note;

    public ShiftNoteDto() {
    }


    public ShiftNoteDto(String employeeName, String shiftName, LocalDateTime shiftDate, String note) {
        this.employeeName = employeeName;
        this.shiftName = shiftName;
        this.shiftDate = shiftDate;
        this.note = note;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public LocalDateTime getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDateTime shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}


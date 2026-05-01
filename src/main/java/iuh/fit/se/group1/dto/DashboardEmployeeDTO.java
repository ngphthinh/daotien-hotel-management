package iuh.fit.se.group1.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DashboardEmployeeDTO implements Serializable {
    private DashboardSummaryDto summaryData;
    private List<RevenueSourceDto> revenueSources;
    private List<PeakHourDto> peakHours;
    private RoomStatusDto roomStatus;
    private WarningDto warnings;
    private List<ShiftNoteDto> shiftNotes;
}

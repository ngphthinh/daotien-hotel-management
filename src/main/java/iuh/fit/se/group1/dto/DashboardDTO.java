package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DashboardDTO implements Serializable {
    private DashboardSummaryDto summaryData;
    private List<RevenueSourceDto> revenueSources;
    private List<PeakHourDto> peakHours;
    private WarningDto warnings;
    private BigDecimal periodRevenue;
    private int currentGuestCount;

}
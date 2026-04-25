package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WarningDto {
    private int lateCheckOutCount;
    private int brokenRoomsCount;
    private boolean hasNewVersion;


}


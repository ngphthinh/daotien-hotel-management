package iuh.fit.se.group1.dto;

/**
 * DTO cho cảnh báo
 */
public class WarningDto {
    private int lateCheckOutCount;
    private int brokenRoomsCount;
    private boolean hasNewVersion;

    public WarningDto() {
    }

    public WarningDto(int lateCheckOutCount, int brokenRoomsCount, boolean hasNewVersion) {
        this.lateCheckOutCount = lateCheckOutCount;
        this.brokenRoomsCount = brokenRoomsCount;
        this.hasNewVersion = hasNewVersion;
    }

    public int getLateCheckOutCount() {
        return lateCheckOutCount;
    }

    public void setLateCheckOutCount(int lateCheckOutCount) {
        this.lateCheckOutCount = lateCheckOutCount;
    }

    public int getBrokenRoomsCount() {
        return brokenRoomsCount;
    }

    public void setBrokenRoomsCount(int brokenRoomsCount) {
        this.brokenRoomsCount = brokenRoomsCount;
    }

    public boolean isHasNewVersion() {
        return hasNewVersion;
    }

    public void setHasNewVersion(boolean hasNewVersion) {
        this.hasNewVersion = hasNewVersion;
    }
}


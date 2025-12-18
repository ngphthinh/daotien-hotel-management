package iuh.fit.se.group1.enums;

import iuh.fit.se.group1.config.AppLogger;

public enum RoomStatus {
    AVAILABLE("Phòng trống"),
    OCCUPIED("Đang sử dụng"),
    OUT_OF_ORDER("Phòng hỏng");
    private final String displayName;
    RoomStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static RoomStatus fromString(String status) {
        for (RoomStatus rs : RoomStatus.values()) {
            if (rs.name().equalsIgnoreCase(status)) {
                return rs;
            }
        }
        AppLogger.error("No enum constant for status: " + status);
        throw new IllegalArgumentException("No enum constant for status: " + status);
    }

    public static RoomStatus fromDisplayName(String displayName) {
        for (RoomStatus rs : RoomStatus.values()) {
            if (rs.displayName.equalsIgnoreCase(displayName)) {
                return rs;
            }
        }
        AppLogger.error("No enum constant for display name: " + displayName);
        throw new IllegalArgumentException("No enum constant for display name: " + displayName);
    }
}

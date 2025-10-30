package iuh.fit.se.group1.enums;

import iuh.fit.se.group1.config.AppLogger;

public enum RoomStatus {
    AVAILABLE,
    OCCUPIED,
    OUT_OF_ORDER;

    public static RoomStatus fromString(String status) {
        for (RoomStatus rs : RoomStatus.values()) {
            if (rs.name().equalsIgnoreCase(status)) {
                return rs;
            }
        }
        AppLogger.error("No enum constant for status: " + status);
        throw new IllegalArgumentException("No enum constant for status: " + status);
    }
}

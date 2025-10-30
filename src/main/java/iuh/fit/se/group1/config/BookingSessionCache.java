package iuh.fit.se.group1.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingSessionCache {
    private static final Set<Long> pendingRoomIds = new HashSet<>();

    public static boolean isRoomPending(Long roomId) {
        return pendingRoomIds.contains(roomId);
    }

    public static void addPendingRoom(Long roomId) {
        pendingRoomIds.add(roomId);
    }

    public static void removePendingRoom(Long roomId) {
        pendingRoomIds.remove(roomId);
    }

    public static void clearAll() {
        pendingRoomIds.clear();
    }

    public static List<Long> getPendingRoomIds() {
        return List.copyOf(pendingRoomIds);
    }
}

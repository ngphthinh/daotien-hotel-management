package iuh.fit.se.group1.enums;

public enum BookingType {
    HOURLY("Theo giờ"),
    OVERNIGHT("Qua đêm"),
    DAILY("Theo ngày"),
    ;

    private final String displayName;

    BookingType(String displayName) {
        this.displayName = displayName;
    }

    public static BookingType fromString(String str) {
        for (BookingType bt : BookingType.values()) {
            if (bt.name().equalsIgnoreCase(str) || bt.displayName.equalsIgnoreCase(str)) {
                return bt;
            }
        }
        throw new IllegalArgumentException("No enum constant for string: " + str);
    }

    public static BookingType fromIndex(int idx) {
        for (BookingType bt : BookingType.values()) {
            if (bt.ordinal() == idx) {
                return bt;
            }
        }
        throw new IllegalArgumentException("No enum constant for index: " + idx);
    }

    public static int toIndex(BookingType bookingType) {
        switch (bookingType) {
            case HOURLY:
                return 0;
            case DAILY:
                return 1;
            case OVERNIGHT:
                return 2;
            default:
                throw new IllegalArgumentException("Unknown BookingType: " + bookingType);
        }
    }

    public String getDisplayName() {
        return displayName;
    }
}

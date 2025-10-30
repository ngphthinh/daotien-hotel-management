package iuh.fit.se.group1.enums;

public enum BookingType {
    HOURLY,
    OVERNIGHT,
    DAILY;

    public static BookingType fromIndex(int idx){
        for (BookingType bt : BookingType.values()) {
            if (bt.ordinal() == idx) {
                return bt;
            }
        }
        throw new IllegalArgumentException("No enum constant for index: " + idx);
    }
}

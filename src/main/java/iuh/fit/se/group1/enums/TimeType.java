package iuh.fit.se.group1.enums;

public enum TimeType {
    TODAY(1),
    DAYS_7(2),
    DAYS_30(3),
    DAYS_90(4);
    private final int value;

    TimeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
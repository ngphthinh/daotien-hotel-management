package iuh.fit.se.group1.enums;

public enum PaymentType {
    CASH("Tiền mặt"),
    E_WALLET("Chuyển khoản");
    private final String name;

    PaymentType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

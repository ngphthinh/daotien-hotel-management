package iuh.fit.se.group1.enums;

import java.io.Serializable;

public enum PaymentType implements Serializable {
    CASH("Tiền mặt"),
    E_WALLET("Chuyển khoản");
    private final String name;

    PaymentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

package iuh.fit.se.group1.enums;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
public enum OrderBookStatus implements Serializable {
    COMPLETED("Đã hoàn thành"),
    PROCESSING("Đang xử lý"),
    RESERVED("Đặt trước"),
    CANCELLED("Đã hủy");

    private final String label;

    OrderBookStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }


}


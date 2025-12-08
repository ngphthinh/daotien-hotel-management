package iuh.fit.se.group1.dto;

import java.math.BigDecimal;

public class SurchargeDTO {
    private Long surchargeId;
    private String name;
    private BigDecimal price;
    private int quantity;
    public SurchargeDTO() {
    }
    public SurchargeDTO(Long surchargeId, String name, BigDecimal price, int quantity) {
        this.surchargeId = surchargeId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getSurchargeId() {
        return surchargeId;
    }

    public void setSurchargeId(Long surchargeId) {
        this.surchargeId = surchargeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name;
    }
}

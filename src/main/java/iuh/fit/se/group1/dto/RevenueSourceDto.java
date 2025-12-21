package iuh.fit.se.group1.dto;

import java.math.BigDecimal;

/**
 * DTO cho nguồn doanh thu theo loại booking
 */
public class RevenueSourceDto {
    private String source;
    private BigDecimal amount;
    private double percentage;

    public RevenueSourceDto() {
    }

    public RevenueSourceDto(String source, BigDecimal amount, double percentage) {
        this.source = source;
        this.amount = amount;
        this.percentage = percentage;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}


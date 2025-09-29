package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Payment {
    private Long paymentId;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private PaymentType paymentType;
    private LocalDate createdAt;

    public Payment(Long paymentId, LocalDate paymentDate, BigDecimal amount, PaymentType paymentType, LocalDate createdAt) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentType = paymentType;
        this.createdAt = createdAt;
    }

    public Payment() {
    }

    public Payment(LocalDate paymentDate, BigDecimal amount, PaymentType paymentType) {
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentType = paymentType;
        this.createdAt = LocalDate.now();
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Payment payment)) return false;
        return Objects.equals(paymentId, payment.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(paymentId);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                ", paymentType=" + paymentType +
                ", createdAt=" + createdAt +
                '}';
    }
}

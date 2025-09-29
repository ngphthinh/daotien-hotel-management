package iuh.fit.se.group1.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Feedback {
    private Long feedbackId;
    private String content;
    private Customer customer;
    private int rating;
    private LocalDate createdAt;
    public Feedback() {
    }
    public Feedback(Long feedbackId, String content, Customer customer, int rating, LocalDate createdAt) {
        this.feedbackId = feedbackId;
        this.content = content;
        this.customer = customer;
        this.rating = rating;
        this.createdAt = createdAt;
    }
    public Long getFeedbackId() {
        return feedbackId;
    }
    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Feedback feedback)) return false;
        return Objects.equals(feedbackId, feedback.feedbackId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(feedbackId);
    }

    @Override
    public String toString() {
        return "Feedback [feedbackId=" + feedbackId + ", content=" + content + ", customer=" + customer + ", rating="
                + rating + ", createdAt=" + createdAt + "]";
    }
    
}

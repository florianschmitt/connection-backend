package de.florianschmitt.model.rest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EPaymentDTO implements Serializable {

    private Long id;

    private String requestId;

    private BigDecimal paymentReceived;

    private String paymentBookedBy;

    private LocalDateTime paymentBookedAt;

    private String comment;

    @java.beans.ConstructorProperties({"id", "requestId", "paymentReceived", "paymentBookedBy", "paymentBookedAt", "comment"})
    public EPaymentDTO(Long id, String requestId, BigDecimal paymentReceived, String paymentBookedBy, LocalDateTime paymentBookedAt, String comment) {
        this.id = id;
        this.requestId = requestId;
        this.paymentReceived = paymentReceived;
        this.paymentBookedBy = paymentBookedBy;
        this.paymentBookedAt = paymentBookedAt;
        this.comment = comment;
    }

    public EPaymentDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public BigDecimal getPaymentReceived() {
        return this.paymentReceived;
    }

    public String getPaymentBookedBy() {
        return this.paymentBookedBy;
    }

    public LocalDateTime getPaymentBookedAt() {
        return this.paymentBookedAt;
    }

    public String getComment() {
        return this.comment;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setPaymentReceived(BigDecimal paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public void setPaymentBookedBy(String paymentBookedBy) {
        this.paymentBookedBy = paymentBookedBy;
    }

    public void setPaymentBookedAt(LocalDateTime paymentBookedAt) {
        this.paymentBookedAt = paymentBookedAt;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

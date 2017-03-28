package de.florianschmitt.model.rest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EPaymentDTO implements Serializable {

    private Long id;

    private String requestId;

    private BigDecimal paymentReceived;

    private String paymentBookedBy;

    private LocalDateTime paymentBookedAt;

    private String comment;
}

package de.florianschmitt.model.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class EPayment : BaseEntity {

    constructor(request: ERequest, paymentReceived: BigDecimal, paymentBookedBy: ESystemUser, comment: String?) {
        this.request = request
        this.paymentReceived = paymentReceived
        this.paymentBookedBy = paymentBookedBy
        this.comment = comment
        this.paymentBookedAt = LocalDateTime.now()
    }

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    var request: ERequest

    @Column(nullable = true, precision = 10, scale = 2)
    var paymentReceived: BigDecimal? = null

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    var paymentBookedBy: ESystemUser

    @Column(nullable = false)
    @NotNull
    var paymentBookedAt: LocalDateTime
        private set

    @Column(nullable = true, length = 1024)
    var comment: String?
}

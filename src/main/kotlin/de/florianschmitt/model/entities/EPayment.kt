package de.florianschmitt.model.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class EPayment(request: ERequest, paymentReceived: BigDecimal, paymentBookedBy: ESystemUser, comment: String?) : BaseEntity() {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    var request: ERequest = request

    @Column(nullable = true, precision = 10, scale = 2)
    var paymentReceived: BigDecimal?  = paymentReceived

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    var paymentBookedBy: ESystemUser = paymentBookedBy

    @Column(nullable = false)
    @NotNull
    var paymentBookedAt: LocalDateTime = LocalDateTime.now()
        private set

    @Column(nullable = true, length = 1024)
    var comment: String? = comment
}

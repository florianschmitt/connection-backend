package de.florianschmitt.model.rest

import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

class EPaymentDTO : Serializable {

    var id: Long? = null

    @NotNull
    var requestId: String? = null

    @NotNull
    var paymentReceived: BigDecimal? = null

    var paymentBookedBy: String? = null

    var paymentBookedAt: LocalDateTime? = null

    var comment: String? = null
}

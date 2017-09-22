package de.florianschmitt.model.rest

import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

class EPaymentDTO : Serializable {

    var id: Long? = null

    var requestId: String? = null

    var paymentReceived: BigDecimal? = null

    var paymentBookedBy: String? = null

    var paymentBookedAt: LocalDateTime? = null

    var comment: String? = null
}

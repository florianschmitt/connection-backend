package de.florianschmitt.service

import de.florianschmitt.model.entities.EPayment
import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.ESystemUser
import de.florianschmitt.repository.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class PaymentService {

    @Autowired
    private lateinit var repository: PaymentRepository

    @Transactional
    fun processPayment(request: ERequest, value: BigDecimal, bookedBy: ESystemUser, comment: String?) {
        // TODO: fail if not finished?
        repository.save(EPayment(request, value, bookedBy, comment))
    }

    fun findAll(pageable: Pageable) = repository.findAll(pageable)

    fun findForRequest(requestIdentifier: String, pageable: Pageable) = repository.findByRequest_RequestIdentifier(requestIdentifier, pageable)
}
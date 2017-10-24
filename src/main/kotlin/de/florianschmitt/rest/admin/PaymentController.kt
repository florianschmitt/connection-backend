package de.florianschmitt.rest.admin

import de.florianschmitt.model.entities.ESystemUser
import de.florianschmitt.model.rest.EPaymentDTO
import de.florianschmitt.service.PaymentService
import de.florianschmitt.service.RequestService
import de.florianschmitt.service.util.DTOMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/admin/payment")
internal class PaymentController {

    @Autowired
    private lateinit var service: PaymentService

    @Autowired
    private lateinit var requestService: RequestService

    @Autowired
    private lateinit var mapper: DTOMapper

    @PostMapping(path = arrayOf("/placePayment"))
    @ResponseStatus(HttpStatus.OK)
    fun placePayment(@RequestBody @Valid data: EPaymentDTO) {
        val request = requestService.findByRequestIdentifier(data.requestId!!)
        service.processPayment(request, data.paymentReceived!!, Helper.currentUser, data.comment)
    }

    @GetMapping(path = arrayOf("/all"), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun all(): ResponseEntity<Page<EPaymentDTO>> {
        val pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort(Sort.Direction.DESC, "paymentBookedAt"))
        val entities = service.findAll(pageable)
        val result = mapper.map(entities, EPaymentDTO::class.java)
        return ResponseEntity.ok(result)
    }

    @GetMapping(path = arrayOf("/forRequest"), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun forRequest(@RequestParam("requestIdentifier") requestIdentifier: String): ResponseEntity<Page<EPaymentDTO>> {
        val pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort(Sort.Direction.DESC, "paymentBookedAt"))
        val entities = service.findForRequest(requestIdentifier, pageable)
        val result = mapper.map(entities, EPaymentDTO::class.java)
        return ResponseEntity.ok(result)
    }
}

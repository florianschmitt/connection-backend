package de.florianschmitt.rest

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.rest.ERequestDTO
import de.florianschmitt.rest.util.HasRequesterRole
import de.florianschmitt.service.RequestService
import de.florianschmitt.service.util.DTOMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

const val answerRequest = "answerRequest"
const val placeRequest = "placeRequest"
const val declineRequest = "declineRequest"

@RestController
@HasRequesterRole
internal class RequestController {

    @Autowired
    private lateinit var mapper: DTOMapper

    @Autowired
    private lateinit var service: RequestService

    @PostMapping(path = arrayOf("/$placeRequest"))
    fun placeRequest(@RequestBody @Valid data: ERequestDTO): ResponseEntity<PlaceRequestResult> {
        val request = mapper.map(data, ERequest::class.java)
        val requestId = service.submitNewRequest(request)
        return ResponseEntity.ok(PlaceRequestResult(requestId))
    }

    @GetMapping(path = arrayOf("/$declineRequest/{requestIdentifier}"))
    fun declineRequest(@PathVariable(name = "requestIdentifier") requestIdentifier: String): ResponseEntity<*> {
        service.declineRequest(requestIdentifier)
        return ResponseEntity.noContent().build<Any>()
    }

    @PostMapping(path = arrayOf("/$answerRequest/{voucherIdentifier}/yes"))
    fun answerRequestYes(@PathVariable(name = "voucherIdentifier") voucherIdentifier: String): ResponseEntity<*> {
        service.answerRequest(voucherIdentifier, true)
        return ResponseEntity.noContent().build<Any>()
    }

    @PostMapping(path = arrayOf("/$answerRequest/{voucherIdentifier}/no"))
    fun answerRequestNo(@PathVariable(name = "voucherIdentifier") voucherIdentifier: String): ResponseEntity<*> {
        service.answerRequest(voucherIdentifier, false)
        return ResponseEntity.noContent().build<Any>()
    }

    @GetMapping(path = arrayOf("/$answerRequest/{voucherIdentifier}/status"))
    fun answerRequestGet(@PathVariable(name = "voucherIdentifier") voucherIdentifier: String): ResponseEntity<*> {
        service.checkVoucherValid(voucherIdentifier)
        return ResponseEntity.noContent().build<Any>()
    }
}

data class PlaceRequestResult(val requestId: String) {
    constructor() : this("") // needed for JSON deserializatoin
}

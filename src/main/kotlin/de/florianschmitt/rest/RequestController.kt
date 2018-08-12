package de.florianschmitt.rest

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.rest.ERequestSimpleDTO
import de.florianschmitt.model.rest.ERequestDTO
import de.florianschmitt.service.RequestService
import de.florianschmitt.service.util.DTOMapper
import de.florianschmitt.service.util.toSimpleDto
import de.florianschmitt.system.util.HasRequesterRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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

    @PostMapping(path = ["/$placeRequest"])
    fun placeRequest(@RequestBody @Valid data: ERequestDTO): ResponseEntity<PlaceRequestResult> {
        val request = mapper.map(data, ERequest::class.java)
        val requestId = service.submitNewRequest(request)
        return ResponseEntity.ok(PlaceRequestResult(requestId))
    }

    @GetMapping(path = ["/getRequest/{voucherIdentifier}"])
    fun getRequest(@PathVariable(name = "voucherIdentifier") voucherIdentifier: String): ERequestSimpleDTO {
        val request = service.findByVoucherIdentifier(voucherIdentifier)
        return request.toSimpleDto()
    }

    @GetMapping(path = ["/$declineRequest/{requestIdentifier}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun declineRequest(@PathVariable(name = "requestIdentifier") requestIdentifier: String) {
        service.declineRequest(requestIdentifier)
    }

    @PostMapping(path = ["/$answerRequest/{voucherIdentifier}/yes"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun answerRequestYes(@PathVariable(name = "voucherIdentifier") voucherIdentifier: String) {
        service.answerRequest(voucherIdentifier, true)
    }

    @PostMapping(path = ["/$answerRequest/{voucherIdentifier}/no"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun answerRequestNo(@PathVariable(name = "voucherIdentifier") voucherIdentifier: String) {
        service.answerRequest(voucherIdentifier, false)
    }

    @GetMapping(path = ["/$answerRequest/{voucherIdentifier}/status"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun answerRequestStatus(@PathVariable(name = "voucherIdentifier") voucherIdentifier: String) {
        service.checkVoucherValid(voucherIdentifier)
    }
}

data class PlaceRequestResult(val requestId: String) {
    constructor() : this("") // needed for JSON deserializatoin
}

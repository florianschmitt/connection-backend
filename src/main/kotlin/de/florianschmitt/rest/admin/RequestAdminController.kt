package de.florianschmitt.rest.admin

import de.florianschmitt.model.entities.EVoucher
import de.florianschmitt.model.rest.ERequestDTO
import de.florianschmitt.model.rest.EVoucherDTO
import de.florianschmitt.service.RequestService
import de.florianschmitt.service.util.DTOMapper
import de.florianschmitt.service.util.toDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/request")
internal class RequestAdminController {

    @Autowired
    private lateinit var service: RequestService

    @Autowired
    private lateinit var mapper: DTOMapper

    @GetMapping(path = arrayOf("/get/{requestIdentifier}"), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun get(@PathVariable(name = "requestIdentifier") requestIdentifier: String): ResponseEntity<ERequestDTO> {
        val request = service.findByRequestIdentifier(requestIdentifier)
        val result = mapper.map(request, ERequestDTO::class.java)
        return ResponseEntity.ok(result)
    }

    @GetMapping(path = arrayOf("/getAnswers/{requestIdentifier}"), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun getAnswers(@PathVariable(name = "requestIdentifier") requestIdentifier: String): ResponseEntity<Collection<EVoucherDTO>> {
        val vouchers = service.listVouchers(requestIdentifier)
        val result = vouchers.map(EVoucher::toDto)
        return ResponseEntity.ok(result)
    }

    @GetMapping(path = arrayOf("/all"), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun all(): ResponseEntity<Page<ERequestDTO>> {
        val pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "createdAt"))
        val page = service.all(pageable)
        val result = mapper.map(page, ERequestDTO::class.java)
        return ResponseEntity.ok(result)
    }

    @GetMapping(path = arrayOf("/notPayed"), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun notPayed(): ResponseEntity<Page<ERequestDTO>> {
        val pageable = PageRequest.of(0, Integer.MAX_VALUE)
        val page = service.findAllNotPayed(pageable)
        val result = mapper.map(page, ERequestDTO::class.java)
        return ResponseEntity.ok(result)
    }
}

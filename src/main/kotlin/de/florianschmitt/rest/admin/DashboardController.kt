package de.florianschmitt.rest.admin

import de.florianschmitt.model.rest.EDashboardDTO
import de.florianschmitt.service.DashboardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/dashboard")
internal class DashboardController {

    @Autowired
    private lateinit var service: DashboardService

    @RequestMapping(path = arrayOf("/info"), method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun get(): ResponseEntity<EDashboardDTO> = ResponseEntity.ok(service.info())
}

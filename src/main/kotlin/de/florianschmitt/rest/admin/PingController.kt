package de.florianschmitt.rest.admin

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class PingController {

    @GetMapping(path = arrayOf("/ping"))
    @ResponseStatus(HttpStatus.OK)
    fun ping() = {}
}

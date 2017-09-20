package de.florianschmitt.rest.admin

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
internal class PingController {

    @GetMapping(path = arrayOf("/ping"))
    fun ping() = ResponseEntity.ok()
}

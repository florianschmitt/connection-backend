package de.florianschmitt.rest

import de.florianschmitt.model.rest.EFeedbackDTO
import de.florianschmitt.service.FeedbackService
import de.florianschmitt.system.util.HasRequesterRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@HasRequesterRole
internal class FeedbackController {

    @Autowired
    private lateinit var service: FeedbackService

    @PostMapping(path = arrayOf("/feedback/{requestIdentifier}"))
    fun feedback(@PathVariable(name = "requestIdentifier") requestIdentifier: String,
                 @RequestBody @Valid data: EFeedbackDTO): ResponseEntity<*> {
        service.feedback(requestIdentifier, data.positive, data.comment)
        return ResponseEntity.ok()
                .build<Any>()
    }
}

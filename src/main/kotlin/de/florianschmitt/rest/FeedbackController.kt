package de.florianschmitt.rest

import de.florianschmitt.model.rest.EFeedbackDTO
import de.florianschmitt.service.FeedbackService
import de.florianschmitt.system.util.HasRequesterRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@HasRequesterRole
internal class FeedbackController {

    @Autowired
    private lateinit var service: FeedbackService

    @PostMapping(path = arrayOf("/feedback/{requestIdentifier}"))
    @ResponseStatus(HttpStatus.OK)
    fun feedback(@PathVariable(name = "requestIdentifier") requestIdentifier: String,
                 @RequestBody @Valid data: EFeedbackDTO) {
        service.feedback(requestIdentifier, data.positive, data.comment)
    }
}

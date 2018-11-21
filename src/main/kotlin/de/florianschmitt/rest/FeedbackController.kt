package de.florianschmitt.rest

import de.florianschmitt.model.rest.EFeedbackDTO
import de.florianschmitt.model.rest.EFeedbackRequesterDTO
import de.florianschmitt.service.FeedbackRequesterService
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
    private lateinit var feedbackService: FeedbackService

    @Autowired
    private lateinit var feedbackRequesterService: FeedbackRequesterService


    @PostMapping(path = ["/feedbackvolunteer/{requestIdentifier}"])
    @ResponseStatus(HttpStatus.OK)
    fun feedbackVolunteer(@PathVariable(name = "requestIdentifier") requestIdentifier: String,
                          @RequestBody @Valid data: EFeedbackDTO) {
        feedbackService.feedback(requestIdentifier, data.positive, data.comment)
    }

    @PostMapping(path = ["/feedbackrequester/{requestIdentifier}"])
    @ResponseStatus(HttpStatus.OK)
    fun feedbackRequester(@PathVariable(name = "requestIdentifier") requestIdentifier: String,
                          @RequestBody @Valid data: EFeedbackRequesterDTO) {
        feedbackRequesterService.feedback(requestIdentifier, data.hasOccurred,
                data.wasPositive, data.wasCanceled, data.comment)
    }
}

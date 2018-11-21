package de.florianschmitt.rest

import de.florianschmitt.model.rest.EFeedbackDTO
import de.florianschmitt.model.rest.EFeedbackVolunteerDTO
import de.florianschmitt.service.FeedbackVolunteerService
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
    private lateinit var feedbackVolunteerService: FeedbackVolunteerService

    @PostMapping(path = ["/feedbackrequester/{requestIdentifier}"])
    @ResponseStatus(HttpStatus.OK)
    fun feedbackRequester(@PathVariable(name = "requestIdentifier") requestIdentifier: String,
                          @RequestBody @Valid data: EFeedbackDTO) {
        feedbackService.feedback(requestIdentifier, data.positive, data.comment)
    }

    @PostMapping(path = ["/feedbackvolunteer/{requestIdentifier}"])
    @ResponseStatus(HttpStatus.OK)
    fun feedbackVolunteer(@PathVariable(name = "requestIdentifier") requestIdentifier: String,
                          @RequestBody @Valid data: EFeedbackVolunteerDTO) {
        feedbackVolunteerService.feedback(requestIdentifier, data.hasOccurred,
                data.wasPositive, data.wasCanceled, data.comment)
    }
}

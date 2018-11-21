package de.florianschmitt.rest.admin

import de.florianschmitt.model.rest.EFeedbackDTO
import de.florianschmitt.model.rest.EFeedbackVolunteerDTO
import de.florianschmitt.service.FeedbackVolunteerService
import de.florianschmitt.service.FeedbackService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/feedback")
internal class FeedbackAdminController {

    @Autowired
    private lateinit var service: FeedbackService

    @Autowired
    private lateinit var feedbackVolunteerService: FeedbackVolunteerService

    @GetMapping(path = ["/requester/{requestIdentifier}"])
    fun getFeedbackRequester(@PathVariable(name = "requestIdentifier") requestIdentifier: String): ResponseEntity<EFeedbackDTO> {
        val feedback = service.findFeedback(requestIdentifier) ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(EFeedbackDTO(
                positive = feedback.positive,
                comment = feedback.comment))
    }

    @GetMapping(path = ["/volunteer/{requestIdentifier}"])
    fun getFeedbackVolunteer(@PathVariable(name = "requestIdentifier") requestIdentifier: String): ResponseEntity<EFeedbackVolunteerDTO> {
        val feedback = feedbackVolunteerService.findFeedback(requestIdentifier) ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(EFeedbackVolunteerDTO(
                hasOccurred = feedback.hasOccurred,
                wasPositive = feedback.wasPositive,
                wasCanceled = feedback.wasCanceled,
                comment = feedback.comment))
    }
}

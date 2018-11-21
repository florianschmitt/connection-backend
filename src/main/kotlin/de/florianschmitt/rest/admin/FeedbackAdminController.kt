package de.florianschmitt.rest.admin

import de.florianschmitt.model.rest.EFeedbackDTO
import de.florianschmitt.model.rest.EFeedbackRequesterDTO
import de.florianschmitt.service.FeedbackRequesterService
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
    private lateinit var feedbackRequesterService: FeedbackRequesterService

    @GetMapping(path = ["/volunteer/{requestIdentifier}"])
    fun getFeedback(@PathVariable(name = "requestIdentifier") requestIdentifier: String): ResponseEntity<EFeedbackDTO> {
        val feedback = service.findFeedback(requestIdentifier) ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(EFeedbackDTO(
                positive = feedback.positive,
                comment = feedback.comment))
    }

    @GetMapping(path = ["/requester/{requestIdentifier}"])
    fun getFeedbackRequester(@PathVariable(name = "requestIdentifier") requestIdentifier: String): ResponseEntity<EFeedbackRequesterDTO> {
        val feedback = feedbackRequesterService.findFeedback(requestIdentifier) ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(EFeedbackRequesterDTO(
                hasOccurred = feedback.hasOccurred,
                wasPositive = feedback.wasPositive,
                wasCanceled = feedback.wasCanceled,
                comment = feedback.comment))
    }
}

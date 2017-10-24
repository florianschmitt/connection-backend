package de.florianschmitt.rest.admin

import de.florianschmitt.model.rest.EFeedbackDTO
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

    @GetMapping(path = arrayOf("/get/{requestIdentifier}"))
    fun getFeedback(@PathVariable(name = "requestIdentifier") requestIdentifier: String): ResponseEntity<EFeedbackDTO> {
        val feedback = service.findFeedback(requestIdentifier) ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(EFeedbackDTO(
                positive = feedback.positive,
                comment = feedback.comment))
    }
}

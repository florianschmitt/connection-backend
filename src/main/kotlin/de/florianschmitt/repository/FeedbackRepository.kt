package de.florianschmitt.repository

import de.florianschmitt.model.entities.EFeedback
import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.EVolunteer
import org.springframework.data.repository.CrudRepository
import java.util.*

interface FeedbackRepository : CrudRepository<EFeedback, Long> {
    fun findByVolunteerAndRequest(volunteer: EVolunteer, request: ERequest): Optional<EFeedback>

    fun findByRequest(request: ERequest): Optional<EFeedback>

}

package de.florianschmitt.repository

import de.florianschmitt.model.entities.EFeedbackVolunteer
import de.florianschmitt.model.entities.ERequest
import org.springframework.data.repository.CrudRepository
import java.util.*

interface FeedbackVolunteerRepository : CrudRepository<EFeedbackVolunteer, Long> {

    fun findByRequest(request: ERequest): Optional<EFeedbackVolunteer>
}

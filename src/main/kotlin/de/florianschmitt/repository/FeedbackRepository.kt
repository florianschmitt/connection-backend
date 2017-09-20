package de.florianschmitt.repository

import java.util.Optional

import org.springframework.data.repository.CrudRepository

import de.florianschmitt.model.entities.EFeedback
import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.EVolunteer

interface FeedbackRepository : CrudRepository<EFeedback, Long> {
    fun findByVolunteerAndRequest(volunteer: EVolunteer, request: ERequest): Optional<EFeedback>
}

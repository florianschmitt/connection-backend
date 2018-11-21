package de.florianschmitt.repository

import de.florianschmitt.model.entities.EFeedbackRequester
import de.florianschmitt.model.entities.ERequest
import org.springframework.data.repository.CrudRepository
import java.util.*

interface FeedbackRequesterRepository : CrudRepository<EFeedbackRequester, Long> {

    fun findByRequest(request: ERequest): Optional<EFeedbackRequester>
}

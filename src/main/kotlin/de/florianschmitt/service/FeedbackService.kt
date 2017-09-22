package de.florianschmitt.service

import de.florianschmitt.model.entities.EFeedback
import de.florianschmitt.model.entities.ERequestStateEnum
import de.florianschmitt.repository.FeedbackRepository
import de.florianschmitt.repository.RequestRepository
import de.florianschmitt.rest.exception.RequestNotFoundException
import de.florianschmitt.rest.exception.RequestNotYetFinishedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedbackService {

    @Autowired
    private lateinit var repository: FeedbackRepository

    @Autowired
    private lateinit var requestRepository: RequestRepository

    @Transactional
    fun feedback(requestIdentifier: String, positive: Boolean, comment: String?) {
        val request = requestRepository.findByRequestIdentifier(requestIdentifier)
                .orElseThrow({ RequestNotFoundException() })

        if (request.state != ERequestStateEnum.FINISHED) {
            throw RequestNotYetFinishedException()
        }

        val volunteer = request.acceptedByVolunteer!!
        val feedback = EFeedback(volunteer, request, positive, comment)
        repository.save(feedback)
    }
}

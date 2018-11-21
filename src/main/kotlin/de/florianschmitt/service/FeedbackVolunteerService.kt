package de.florianschmitt.service

import de.florianschmitt.model.entities.EFeedbackVolunteer
import de.florianschmitt.model.entities.ERequestStateEnum
import de.florianschmitt.repository.FeedbackVolunteerRepository
import de.florianschmitt.repository.RequestRepository
import de.florianschmitt.rest.exception.FeedbackAlreadyGivenException
import de.florianschmitt.rest.exception.RequestNotFoundException
import de.florianschmitt.rest.exception.RequestNotYetFinishedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedbackVolunteerService {

    @Autowired
    private lateinit var repository: FeedbackVolunteerRepository

    @Autowired
    private lateinit var requestRepository: RequestRepository

    @Transactional
    fun feedback(requestIdentifier: String, hasOccured: Boolean, wasPositive: Boolean?,
                 wasCanceled: Boolean?, comment: String?) {
        val request = requestRepository.findByRequestIdentifier(requestIdentifier)
                .orElseThrow { RequestNotFoundException() }

        if (request.state != ERequestStateEnum.FINISHED) {
            throw RequestNotYetFinishedException()
        }

        if (repository.findByRequest(request).isPresent) {
            throw FeedbackAlreadyGivenException()
        }

        val feedback = EFeedbackVolunteer(request, hasOccured, wasPositive, wasCanceled, comment)
        repository.save(feedback)
    }

    fun findFeedback(requestIdentifier: String) : EFeedbackVolunteer? {
        val request = requestRepository.findByRequestIdentifier(requestIdentifier)
                .orElseThrow { RequestNotFoundException() }
        return repository.findByRequest(request).orElse(null)
    }
}

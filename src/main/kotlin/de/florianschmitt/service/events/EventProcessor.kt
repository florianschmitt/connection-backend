package de.florianschmitt.service.events

import de.florianschmitt.model.entities.ERequestStateEnum
import de.florianschmitt.service.MailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class EventProcessor {

    @Autowired
    private lateinit var mailService: MailService

    @EventListener
    fun handleRequestWasSubmittedEvent(event: RequestWasSubmittedEvent) {
        val request = event.request
        mailService.requestConfirmation(request)

        event.vouchers.forEach {
            mailService.requestAskVolunteer(request, it)
        }
    }

    @EventListener
    fun handleWasAcceptedEvent(event: RequestWasAcceptedEvent) {
        mailService.requestAcceptedConfirmationForVolunteer(event.request)
        mailService.requestAcceptedConfirmationForRequester(event.request)
    }

    @EventListener
    fun handleIsExpiredEvent(event: RequestIsExpiredEvent) {
        mailService.requestExpired(event.request)
    }

    @EventListener
    fun handleIsCanceledEvent(event: RequestWasCanceledEvent) {
        if (event.previousState == ERequestStateEnum.ACCEPTED) {
            // val acceptedByVolunteer = request.acceptedByVolunteer
            // TODO: inform volunteer
        }
    }
}
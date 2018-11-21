package de.florianschmitt.service.events

import de.florianschmitt.model.entities.ERequestStateEnum
import de.florianschmitt.service.MailService
import de.florianschmitt.system.util.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class EventProcessor {

    @Autowired
    private lateinit var mailService: MailService

    @EventListener
    fun handleRequestWasSubmittedEvent(event: RequestWasSubmittedEvent) {
        log.info("request id='${event.request.requestIdentifier}' was submitted")

        mailService.requestConfirmation(event.request)

        event.vouchers.forEach {
            mailService.requestAskVolunteer(event.request, it)
        }
    }

    @EventListener
    fun handleWasAcceptedEvent(event: RequestWasAcceptedEvent) {
        log.info("request id='${event.request.requestIdentifier}' was accepted")

        mailService.requestAcceptedConfirmationForVolunteer(event.request)
        mailService.requestAcceptedConfirmationForRequester(event.request)
    }

    @EventListener
    fun handleIsExpiredEvent(event: RequestIsExpiredEvent) {
        log.info("request id='${event.request.requestIdentifier}' is expired")

        mailService.requestExpired(event.request)
    }

    @EventListener
    fun handleIsCanceledEvent(event: RequestWasCanceledEvent) {
        log.info("request id='${event.request.requestIdentifier}' was canceled")

        if (event.previousState == ERequestStateEnum.ACCEPTED) {
            mailService.requestCanceledInformVolunteer(event.request)
        }
    }

    @EventListener
    fun handleIsFinishedEvent(event: RequestIsFinishedEvent) {
        log.info("request id='${event.request.requestIdentifier}' is finished")
        mailService.requestFinishedInformVolunteer(event.request)
        mailService.requestFinishedInformRequester(event.request)
    }
}
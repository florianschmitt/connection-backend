package de.florianschmitt.service

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.EVoucher
import de.florianschmitt.model.fixtures.TemplateFixtures
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MailService {
    @Autowired
    private lateinit var mailSendService: MailSendService

    @Autowired
    private lateinit var templateService: TemplateService

    @Autowired
    private lateinit var stringTemplateService: StringTemplateService

    fun requestConfirmation(request: ERequest) {
        val subjectTemplate = templateService.getTemplate(TemplateFixtures.REQUEST_CONFIRMATION_SUBJECT)
        val contentTemplate = templateService.getTemplate(TemplateFixtures.REQUEST_CONFIRMATION_CONTENT)

        val subject = stringTemplateService.replace(subjectTemplate, request.requesterName!!, request)
        val content = stringTemplateService.replace(contentTemplate, request.requesterName!!, request)

        mailSendService.send(request.email!!, subject, content)
    }

    fun requestAskVolunteer(request: ERequest, voucher: EVoucher) {
        val subjectTemplate = templateService.getTemplate(TemplateFixtures.REQUEST_ASK_VOLUNTEER_SUBJECT)
        val contentTemplate = templateService.getTemplate(TemplateFixtures.REQUEST_ASK_VOLUNTEER_CONTENT)

        val subject = stringTemplateService.replace(subjectTemplate, voucher, voucher.volunteer, request)
        val content = stringTemplateService.replace(contentTemplate, voucher, voucher.volunteer, request)

        mailSendService.send(voucher.volunteer, subject, content)
    }

    fun requestAcceptedConfirmationForVolunteer(request: ERequest) {
        request.acceptedByVolunteer ?: throw NullPointerException("request.acceptedByVolunteer cannot be null")

        val subjectTemplate = templateService.getTemplate(TemplateFixtures.REQUEST_ACCEPTED_VOLUNTEER_SUBJECT)
        val contentTemplate = templateService.getTemplate(TemplateFixtures.REQUEST_ACCEPTED_VOLUNTEER_CONTENT)

        val subject = stringTemplateService.replace(subjectTemplate, request.acceptedByVolunteer!!.germanDisplayString, request)
        val content = stringTemplateService.replace(contentTemplate, request.acceptedByVolunteer!!.germanDisplayString, request)

        mailSendService.send(request.acceptedByVolunteer!!, subject, content)
    }

    fun requestAcceptedConfirmationForRequester(request: ERequest) {
        request.acceptedByVolunteer ?: throw NullPointerException("request.acceptedByVolunteer cannot be null")

        val subjectTemplate = templateService.getTemplate(TemplateFixtures.REQUEST_ACCEPTED_REQUESTER_SUBJECT)
        val contentTemplate = templateService.getTemplate(TemplateFixtures.REQUEST_ACCEPTED_REQUESTER_CONTENT)

        val subject = stringTemplateService.replace(subjectTemplate, request.requesterName!!, request)
        val content = stringTemplateService.replace(contentTemplate, request.requesterName!!, request)

        mailSendService.send(request.email!!, subject, content)
    }
}

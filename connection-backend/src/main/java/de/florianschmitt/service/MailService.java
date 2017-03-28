package de.florianschmitt.service;

import de.florianschmitt.model.entities.ERequest;
import de.florianschmitt.model.entities.EVoucher;
import de.florianschmitt.model.fixtures.TemplateFixtures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private MailSendService mailSendService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private StringTemplateService stringTemplateService;

    public void requestAskVolunteer(ERequest request, EVoucher voucher) {
        String subjectTemplate = templateService.getTemplate(TemplateFixtures.REQUEST_ASK_VOLUNTEER_SUBJECT);
        String contentTemplate = templateService.getTemplate(TemplateFixtures.REQUEST_ASK_VOLUNTEER_CONTENT);

        String subject = stringTemplateService.replace(subjectTemplate, voucher, voucher.getVolunteer(), request);
        String content = stringTemplateService.replace(contentTemplate, voucher, voucher.getVolunteer(), request);

        mailSendService.send(voucher.getVolunteer(), subject, content);
    }
}

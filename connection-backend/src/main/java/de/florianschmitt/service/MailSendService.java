package de.florianschmitt.service;

import de.florianschmitt.model.entities.EAbstractUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class MailSendService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${application.mail.from:Organisation <info@orga.com>}")
    private String from;

    @Async
    void send(EAbstractUser user, String subject, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.setText(message);
        try {
            mailSender.send(mail);
        } catch (MailException e) {
            log.error("error while sending mail", e);
            throw e;
        }
    }
}

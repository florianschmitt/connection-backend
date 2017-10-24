package de.florianschmitt.service

import de.florianschmitt.model.entities.EAbstractUser
import de.florianschmitt.system.util.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
internal class MailSendService {

    @Autowired
    private lateinit var mailSender: JavaMailSender

    @Value("\${application.mail.from:Organisation <info@orga.com>}")
    private val from: String? = null

    @Async
    fun send(user: EAbstractUser, subject: String, message: String) {
        sendInternal(user.email, subject, message)
    }

    @Async
    fun send(email: String, subject: String, message: String) {
        sendInternal(email, subject, message)
    }

    private fun sendInternal(email: String, subject: String, message: String) {
        val mail = SimpleMailMessage()
        mail.setTo(email)
        mail.from = from
        mail.subject = subject
        mail.text = message
        try {
            mailSender.send(mail)
        } catch (e: MailException) {
            log.error("error while sending mail", e)
            throw e
        }
    }
}

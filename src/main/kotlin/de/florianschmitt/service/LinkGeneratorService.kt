package de.florianschmitt.service

import de.florianschmitt.model.entities.EVoucher
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
internal class LinkGeneratorService {

    @Value("${'$'}{application.applicationUrl}")
    lateinit private var applicationUrl: String

    private val baseUrl: String
        get() = if (!applicationUrl.endsWith("/")) "$applicationUrl/" else applicationUrl

    fun requestAnswer(voucher: EVoucher) = "${baseUrl}answerrequest/${voucher.identifier}}"
    fun requestAnswerDecline(voucher: EVoucher) = "${requestAnswer(voucher)}/decline"
}
package de.florianschmitt.service

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.EVoucher
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
internal class LinkGeneratorService {

    @Value("${'$'}{application.applicationUrl}")
    private lateinit var applicationUrl: String

    private val baseUrl: String
        get() = if (!applicationUrl.endsWith("/")) "$applicationUrl/" else applicationUrl

    fun requestAnswer(voucher: EVoucher) = "${baseUrl}answerrequest/${voucher.identifier}"
    fun requestAnswerDecline(voucher: EVoucher) = "${requestAnswer(voucher)}/decline"
    fun requestCancel(request: ERequest) = "${baseUrl}cancelrequest/${request.requestIdentifier}"

}
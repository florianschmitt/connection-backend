package de.florianschmitt.service

import de.florianschmitt.model.entities.EAbstractUser
import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.EVoucher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.stringtemplate.v4.ST

@Service
class StringTemplateService {

    @Autowired
    private lateinit var linkGeneratorService: LinkGeneratorService

    @Value("\${application.mail.sender:Organisation}")
    private val sender: String? = null

    fun replace(template: String, voucher: EVoucher, to: EAbstractUser, request: ERequest): String {
        val st = ST(template)
        st.add("to", to)
        st.add("request", request)
        st.add("voucher", voucher)
        st.add("sender", sender)
        st.add("linkAnswerRequest", linkGeneratorService.requestAnswer(voucher))
        st.add("linkAnswerRequestDecline", linkGeneratorService.requestAnswerDecline(voucher))
        return st.render()
    }
}

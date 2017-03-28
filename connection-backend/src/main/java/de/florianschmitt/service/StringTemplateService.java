package de.florianschmitt.service;

import de.florianschmitt.model.entities.EAbstractUser;
import de.florianschmitt.model.entities.ERequest;
import de.florianschmitt.model.entities.EVoucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

@Service
public class StringTemplateService {

    @Autowired
    private LinkGeneratorService linkGeneratorService;

    @Value("${application.mail.sender:Organisation}")
    private String sender;

    public String replace(String template, EVoucher voucher, EAbstractUser to, ERequest request) {
        ST st = new ST(template);
        st.add("to", to);
        st.add("request", request);
        st.add("voucher", voucher);
        st.add("sender", sender);
        st.add("linkAnswerRequest", linkGeneratorService.requestAnswer(voucher));
        st.add("linkAnswerRequestDecline", linkGeneratorService.requestAnswerDecline(voucher));
        String result = st.render();
        return result;
    }
}

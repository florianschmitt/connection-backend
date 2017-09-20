package de.florianschmitt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Profile("test")
public class TestMailSenderConfiguration {

    @Bean
    public JavaMailSender testMailSender() {
        JavaMailSender result = new JavaMailSenderImpl();
        return result;
    }
}

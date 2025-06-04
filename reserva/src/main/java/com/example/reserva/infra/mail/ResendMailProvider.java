package com.example.reserva.infra.mail;

import com.ea.backend.domain.notification.application.contracts.MailProvider;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ResendMailProvider implements MailProvider {

    @Value("${api.mail.resend.api_key}")
    private String resendApiKey;

    private final Resend resend;


    public ResendMailProvider() {
        resend = new Resend(resendApiKey);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("EA Espaços Academicos <okami@okami-mail.daviribeiro.com>")
                .to(to)
                .subject(subject)
                .text(body)
                .build();


        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());

        } catch (ResendException e) {
            e.printStackTrace();
        }

    }
}

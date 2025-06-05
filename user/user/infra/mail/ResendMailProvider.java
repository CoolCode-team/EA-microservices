package user.user.infra.mail;

import com.resend.Resend; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import user.user.domain.notification.MailProvider;

@Component
public class ResendMailProvider implements MailProvider {

    private static final Logger logger = LoggerFactory.getLogger(ResendMailProvider.class);

    private final String resendApiKey;
    private final String fromAddress; // Adicionado para configuração
    private final Resend resend;

    @Autowired
    public ResendMailProvider(
            @Value("${api.mail.resend.api_key}") String resendApiKey,
            @Value("${api.mail.from_address}") String fromAddress) { // Supondo que você adicionará esta property
        this.resendApiKey = resendApiKey;
        this.fromAddress = fromAddress;
        this.resend = new Resend(this.resendApiKey);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(this.fromAddress) // Usando o endereço configurável
                .to(to)
                .subject(subject)
                .html(body) // ou .text(body) se for texto puro. Resend geralmente prefere HTML.
                .build();

        try {
            logger.debug("Attempting to send email to: {}, subject: {}", to, subject);
            CreateEmailResponse data = this.resend.emails().send(params);
            logger.info("Email sent successfully to {}. Resend ID: {}", to, data.getId());
        } catch (ResendException e) {
            logger.error("Failed to send email to {} with subject '{}'. Error: {}. Details: {}", 
                         to, subject, e.getMessage(), e.toString(), e);
            // Decida se quer relançar uma exceção customizada aqui baseado no contrato da MailProvider
            // Ex: throw new MailSendingFailedException("Error sending email via Resend", e);
        }
    }
}

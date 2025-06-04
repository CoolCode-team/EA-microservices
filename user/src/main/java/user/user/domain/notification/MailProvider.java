package user.user.domain.notification;

public interface MailProvider {
    void sendEmail(String to, String subject, String body);
}

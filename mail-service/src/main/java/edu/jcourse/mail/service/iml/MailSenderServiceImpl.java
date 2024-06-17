package edu.jcourse.mail.service.iml;

import edu.jcourse.dto.MailParam;
import edu.jcourse.mail.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static edu.jcourse.mail.util.MessageUtil.ACCOUNT_ACTIVATION_MESSAGE;
import static edu.jcourse.mail.util.MessageUtil.ACTIVATION_MAIL_BODY_MESSAGE;

@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.activation.uri}")
    private String activationServiceUri;

    @Override
    public void send(MailParam mailParams) {
        String messageBody = getActivationMailBody(mailParams.id());
        String emailTo = mailParams.emailTo();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(ACCOUNT_ACTIVATION_MESSAGE);
        mailMessage.setText(messageBody);

        javaMailSender.send(mailMessage);
    }

    private String getActivationMailBody(String id) {
        return ACTIVATION_MAIL_BODY_MESSAGE
                .formatted(activationServiceUri)
                .replace("{id}", id);
    }
}
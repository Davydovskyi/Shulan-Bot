package edu.jcourse.mail.service.iml;

import edu.jcourse.dto.MailParam;
import edu.jcourse.mail.service.ConsumerService;
import edu.jcourse.mail.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final MailSenderService mailSenderService;

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.registration-mail}")
    public void consumeRegistrationMail(MailParam mailParams) {
        mailSenderService.send(mailParams);
    }
}
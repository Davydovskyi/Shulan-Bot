package edu.jcourse.dispatcher.service.impl;

import edu.jcourse.dispatcher.controller.UpdateController;
import edu.jcourse.dispatcher.service.AnswerConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class AnswerConsumerImpl implements AnswerConsumer {

    private final UpdateController updateController;

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.answer-message}")
    public void consume(SendMessage sendMessage) {
        updateController.setView(sendMessage);
    }
}
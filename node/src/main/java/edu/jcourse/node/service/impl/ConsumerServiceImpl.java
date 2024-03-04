package edu.jcourse.node.service.impl;

import edu.jcourse.node.service.ConsumerService;
import edu.jcourse.node.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {

    private final ProducerService producerService;

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.text-message-update}")
    public void consumeTextMessageUpdates(Update update) {
        log.info("Node: Text message received");

        SendMessage sendMessage = SendMessage.builder()
                .text("Answer from node")
                .chatId(update.getMessage().getChatId().toString())
                .build();

        producerService.producerAnswer(sendMessage);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.doc-message-update}")
    public void consumeDocMessageUpdates(Update update) {
        log.info("Node: Doc message received");
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.photo-message-update}")
    public void consumePhotoMessageUpdates(Update update) {
        log.info("Node: Photo message received");
    }
}
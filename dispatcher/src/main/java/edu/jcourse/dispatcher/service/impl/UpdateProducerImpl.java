package edu.jcourse.dispatcher.service.impl;

import edu.jcourse.dispatcher.service.UpdateProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
public class UpdateProducerImpl implements UpdateProducer {
    @Override
    public void produce(String rabbitQueue, Update update) {
        log.info(update.getMessage().getText());
    }
}
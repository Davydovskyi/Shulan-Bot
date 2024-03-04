package edu.jcourse.node.service.impl;

import edu.jcourse.node.entity.RawData;
import edu.jcourse.node.repository.RawDataRepository;
import edu.jcourse.node.service.MainService;
import edu.jcourse.node.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainServiceImpl implements MainService {
    private final RawDataRepository rawDataRepository;
    private final ProducerService producerService;

    @Transactional
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        SendMessage sendMessage = SendMessage.builder()
                .text("Answer from node")
                .chatId(update.getMessage().getChatId().toString())
                .build();

        producerService.producerAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataRepository.save(rawData);
    }

    @Override
    public void processDocMessage(Update update) {

    }

    @Override
    public void processPhotoMessage(Update update) {

    }
}
package edu.jcourse.node.service.impl;

import edu.jcourse.jpa.entity.AppUser;
import edu.jcourse.jpa.entity.UserState;
import edu.jcourse.jpa.repository.AppUserRepository;
import edu.jcourse.node.entity.RawData;
import edu.jcourse.node.repository.RawDataRepository;
import edu.jcourse.node.service.MainService;
import edu.jcourse.node.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainServiceImpl implements MainService {
    private final RawDataRepository rawDataRepository;
    private final ProducerService producerService;
    private final AppUserRepository appUserRepository;

    @Transactional
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        Message message = update.getMessage();
        AppUser appUser = findOrSaveAppUser(update);

        SendMessage sendMessage = SendMessage.builder()
                .text("Answer from node")
                .chatId(message.getChatId().toString())
                .build();

        producerService.producerAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataRepository.save(rawData);
    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        return appUserRepository.findByTelegramUserId(telegramUser.getId())
                .orElseGet(() -> {
                    AppUser appUser = AppUser.builder()
                            .telegramUserId(telegramUser.getId())
                            .firstName(telegramUser.getFirstName())
                            .lastName(telegramUser.getLastName())
                            .username(telegramUser.getUserName())
                            .email("telegram%d@mock.com".formatted(telegramUser.getId()))
                            .isActive(true)
                            .userState(UserState.BASIC_STATE)
                            .build();
                    return appUserRepository.save(appUser);
                });
    }

    @Override
    public void processDocMessage(Update update) {

    }

    @Override
    public void processPhotoMessage(Update update) {

    }
}
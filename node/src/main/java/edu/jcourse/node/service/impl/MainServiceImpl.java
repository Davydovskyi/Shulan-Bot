package edu.jcourse.node.service.impl;

import edu.jcourse.jpa.entity.AppUser;
import edu.jcourse.jpa.entity.UserState;
import edu.jcourse.jpa.repository.AppUserRepository;
import edu.jcourse.node.entity.RawData;
import edu.jcourse.node.repository.RawDataRepository;
import edu.jcourse.node.service.MainService;
import edu.jcourse.node.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static edu.jcourse.jpa.entity.UserState.BASIC_STATE;
import static edu.jcourse.jpa.entity.UserState.WAIT_FOR_EMAIL_STATE;
import static edu.jcourse.node.service.enums.ServiceCommand.*;
import static edu.jcourse.node.util.MessageUtil.*;

@Slf4j
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

        UserState userState = appUser.getUserState();
        String text = message.getText();
        String response = "";

        if (CANCEL.getCmd().equals(text)) {
            response = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            response = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            //TODO добавить обработку емейла
        } else {
            log.error("Unknown user state: " + userState);
            response = UNKNOWN_ERROR_MESSAGE;
        }

        sendAnswer(response, message.getChatId());
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
                            .userState(BASIC_STATE)
                            .build();
                    return appUserRepository.save(appUser);
                });
    }

    @Transactional
    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);

        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();
        if (isAllowedToSendContent(chatId, appUser)) {
            //TODO добавить сохранения документа
            sendAnswer(DOC_IS_DOWNLOADED_MESSAGE, chatId);
        }
    }

    @Transactional
    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);

        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isAllowedToSendContent(chatId, appUser)) {
            //TODO добавить сохранения фото
            sendAnswer(PHOTO_IS_DOWNLOADED_MESSAGE, chatId);
        }
    }

    private boolean isAllowedToSendContent(Long chatId, AppUser appUser) {
        UserState userState = appUser.getUserState();
        if (!appUser.isActive()) {
            sendAnswer(USER_NOT_ACTIVE_MESSAGE, chatId);
            return false;
        } else if (!BASIC_STATE.equals(userState)) {
            sendAnswer(CANCEL_CURRENT_COMMAND_MESSAGE, chatId);
            return false;
        }
        return true;
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .text(output)
                .chatId(chatId)
                .build();
        producerService.producerAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        if (REGISTRATION.getCmd().equals(cmd)) {
            //TODO добавить регистрацию
            return "Registration is not implemented yet!";
        } else if (HELP.getCmd().equals(cmd)) {
            return HELP_MESSAGE;
        } else if (START.getCmd().equals(cmd)) {
            return START_MESSAGE;
        } else {
            return UNKNOWN_COMMAND_MESSAGE;
        }
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setUserState(BASIC_STATE);
        appUserRepository.saveAndFlush(appUser);
        return CANCEL_PROCESS_MESSAGE;
    }
}
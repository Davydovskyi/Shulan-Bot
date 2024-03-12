package edu.jcourse.node.service.impl;

import edu.jcourse.jpa.entity.AppDocument;
import edu.jcourse.jpa.entity.AppPhoto;
import edu.jcourse.jpa.entity.AppUser;
import edu.jcourse.jpa.entity.UserState;
import edu.jcourse.jpa.repository.AppUserRepository;
import edu.jcourse.node.entity.RawData;
import edu.jcourse.node.exception.UploadFileException;
import edu.jcourse.node.repository.RawDataRepository;
import edu.jcourse.node.service.AppUserService;
import edu.jcourse.node.service.FileService;
import edu.jcourse.node.service.MainService;
import edu.jcourse.node.service.ProducerService;
import edu.jcourse.node.service.enums.LinkType;
import edu.jcourse.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static edu.jcourse.jpa.entity.UserState.BASIC_STATE;
import static edu.jcourse.node.service.enums.ServiceCommand.CANCEL;
import static edu.jcourse.node.service.enums.ServiceCommand.find;
import static edu.jcourse.node.util.MessageUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainServiceImpl implements MainService {
    private final RawDataRepository rawDataRepository;
    private final ProducerService producerService;
    private final AppUserRepository appUserRepository;
    private final FileService fileService;
    private final CryptoUtil docCryptoUtil;
    private final CryptoUtil photoCryptoUtil;
    private final AppUserService appUserService;

    @Transactional
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        Message message = update.getMessage();
        AppUser appUser = findOrSaveAppUser(update);

        UserState userState = appUser.getUserState();
        String text = message.getText();
        String response;

        if (CANCEL.getCmd().equals(text)) {
            response = cancelProcess(appUser);
        } else {
            response = switch (userState) {
                case BASIC_STATE -> processServiceCommand(appUser, text);
                case WAIT_FOR_EMAIL_STATE -> appUserService.setEmail(appUser, text);
                default -> {
                    log.error("Unknown user state: " + userState);
                    yield UNKNOWN_ERROR_MESSAGE;
                }
            };
        }

        sendAnswer(response, message.getChatId());
    }

    @Transactional
    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);

        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();
        if (isNotAllowedToSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            String link = fileService.generateLink(doc.getId(), LinkType.GET_DOC, docCryptoUtil);
            sendAnswer(DOC_IS_UPLOADED_MESSAGE.formatted(link), chatId);
        } catch (UploadFileException ex) {
            log.error("Upload file error", ex);
            sendAnswer(UPLOAD_FILE_ERROR_MESSAGE, chatId);
        }
    }

    @Transactional
    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);

        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();
        if (isNotAllowedToSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            String link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO, photoCryptoUtil);
            sendAnswer(PHOTO_IS_UPLOADED_MESSAGE.formatted(link), chatId);
        } catch (UploadFileException ex) {
            log.error("Upload file error", ex);
            sendAnswer(UPLOAD_PHOTO_ERROR_MESSAGE, chatId);
        }
    }

    private boolean isNotAllowedToSendContent(Long chatId, AppUser appUser) {
        UserState userState = appUser.getUserState();
        if (!appUser.isActive()) {
            sendAnswer(USER_NOT_ACTIVE_MESSAGE, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            sendAnswer(CANCEL_CURRENT_COMMAND_MESSAGE, chatId);
            return true;
        }
        return false;
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        return find(cmd)
                .map(serviceCommand -> switch (serviceCommand) {
                    case HELP -> HELP_MESSAGE;
                    case START -> START_MESSAGE;
                    case REGISTRATION -> appUserService.registerUser(appUser);
                    default -> NOT_IMPLEMENTED_YET_MESSAGE;
                })
                .orElse(UNKNOWN_COMMAND_MESSAGE);
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setUserState(BASIC_STATE);
        appUserRepository.saveAndFlush(appUser);
        return CANCEL_PROCESS_MESSAGE;
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
                            .isActive(false)
                            .userState(BASIC_STATE)
                            .build();
                    return appUserRepository.save(appUser);
                });
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataRepository.save(rawData);
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .text(output)
                .chatId(chatId)
                .build();
        producerService.producerAnswer(sendMessage);
    }
}
package edu.jcourse.dispatcher.util;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@UtilityClass
public class MessageUtil {
    public static final String UNSUPPORTED_MESSAGE_TYPE = "Unsupported message type!";
    public static final String FILE_IS_RECEIVED = "File is received! Please, wait...";

    public static SendMessage generateSendMessageWithText(Update update, String text) {
        Message message = update.getMessage();
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(text)
                .build();
    }
}
package edu.jcourse.dispatcher.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramWebhookBot {
    // Long(значит не сразу разрывает соедение с сервером) polling - периодически отправляем запрос на сервер(TG) для получения обновлений ("- есть что для меня? - есть, забирай")
    // Webhook - Мы выставляем наружу какой-то адрес(URL, IP) и говорим серверу(TG) если что-будет то присылай нам. ("-вот тебе точкаб шли сюда вс1 по мере поступления")
    // нужен белый IP + настройка сертефекатов для HTTPS, SSL

    private final UpdateProcessor updateProcessor;
    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.uri}")
    private String botUri;

    public TelegramBot(@Value("${bot.token}") String botToken,
                       UpdateProcessor updateProcessor) {
        super(botToken);
        this.updateProcessor = updateProcessor;
    }

    @PostConstruct
    public void init() {
        updateProcessor.registerBot(this);
        try {
            SetWebhook setWebhook = SetWebhook.builder()
                    .url(botUri)
                    .build();
            setWebhook(setWebhook);
        } catch (TelegramApiException e) {
            log.error("Error setting webhook", e);
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return "/update";
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public void sendAnswerMessage(SendMessage message) {
        if (message == null) {
            return;
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
        }
    }
}
package edu.jcourse.dispatcher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class WebHookController {
    private final UpdateProcessor updateProcessor;

    @PostMapping("/callback/update")
    @ResponseStatus(HttpStatus.OK)
    public void onUpdateReceived(@RequestBody Update update) {
        updateProcessor.processUpdate(update);
    }
}
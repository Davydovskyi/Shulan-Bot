package edu.jcourse.mail.controller;

import edu.jcourse.mail.dto.MailParam;
import edu.jcourse.mail.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailSenderService mailSenderService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public void sendActivationMail(@RequestBody MailParam mailParams) {
        mailSenderService.send(mailParams);
    }
}
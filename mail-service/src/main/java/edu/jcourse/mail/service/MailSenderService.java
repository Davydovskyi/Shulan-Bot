package edu.jcourse.mail.service;

import edu.jcourse.mail.dto.MailParam;

public interface MailSenderService {
    void send(MailParam mailParams);
}
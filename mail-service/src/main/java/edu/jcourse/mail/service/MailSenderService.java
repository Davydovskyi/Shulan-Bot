package edu.jcourse.mail.service;


import edu.jcourse.dto.MailParam;

public interface MailSenderService {
    void send(MailParam mailParams);
}
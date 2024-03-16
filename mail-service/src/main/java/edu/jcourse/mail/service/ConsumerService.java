package edu.jcourse.mail.service;

import edu.jcourse.dto.MailParam;

public interface ConsumerService {

    void consumeRegistrationMail(MailParam mailParams);
}
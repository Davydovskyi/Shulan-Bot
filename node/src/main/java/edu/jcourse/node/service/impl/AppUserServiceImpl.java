package edu.jcourse.node.service.impl;

import edu.jcourse.dto.MailParam;
import edu.jcourse.jpa.entity.AppUser;
import edu.jcourse.jpa.repository.AppUserRepository;
import edu.jcourse.node.service.AppUserService;
import edu.jcourse.util.CryptoUtil;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static edu.jcourse.jpa.entity.UserState.BASIC_STATE;
import static edu.jcourse.jpa.entity.UserState.WAIT_FOR_EMAIL_STATE;
import static edu.jcourse.node.util.MessageUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final CryptoUtil userCryptoUtil;
    private final RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.queues.registration-mail}")
    private String registrationMailQueue;

    @Transactional
    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.isActive()) {
            return USER_ALREADY_REGISTERED_MESSAGE;
        } else if (appUser.getEmail() != null) {
            return EMAIL_SENT_MESSAGE;
        }
        appUser.setUserState(WAIT_FOR_EMAIL_STATE);
        appUserRepository.saveAndFlush(appUser);
        return ENTER_EMAIL_MESSAGE;
    }

    @Transactional
    @Override
    public String setEmail(AppUser appUser, String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            return EMAIL_INVALID_MESSAGE;
        }

        Optional<AppUser> user = appUserRepository.findByEmail(email);
        if (user.isPresent()) {
            return EMAIL_ALREADY_EXIST;
        }

        appUser.setEmail(email);
        appUser.setUserState(BASIC_STATE);
        appUser = appUserRepository.saveAndFlush(appUser);

        String cryptoUserId = userCryptoUtil.encrypt(appUser.getId());
        sendRegistrationMail(cryptoUserId, email);
        return EMAIL_SENT_MESSAGE;
    }

    private void sendRegistrationMail(String cryptoUserId, String email) {
        MailParam mailParams = MailParam.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();
        rabbitTemplate.convertAndSend(registrationMailQueue, mailParams);
    }
}
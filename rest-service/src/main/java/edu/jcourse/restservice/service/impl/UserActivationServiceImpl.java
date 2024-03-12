package edu.jcourse.restservice.service.impl;

import edu.jcourse.jpa.repository.AppUserRepository;
import edu.jcourse.restservice.service.UserActivationService;
import edu.jcourse.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserActivationServiceImpl implements UserActivationService {
    private final AppUserRepository appUserRepository;
    private final CryptoUtil userCryptoUtil;

    @Override
    @Transactional
    public boolean activate(String cryptoUserId) {
        Long userId = userCryptoUtil.decrypt(cryptoUserId);
        return appUserRepository.findById(userId)
                .map(user -> {
                    user.setActive(true);
                    appUserRepository.saveAndFlush(user);
                    return true;
                })
                .orElse(false);
    }
}
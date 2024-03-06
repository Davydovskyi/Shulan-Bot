package edu.jcourse.jpa.repository;

import edu.jcourse.jpa.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByTelegramUserId(Long telegramUserId);
}
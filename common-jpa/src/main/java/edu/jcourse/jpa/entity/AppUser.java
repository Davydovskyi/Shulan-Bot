package edu.jcourse.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "telegram_user_id", unique = true, nullable = false)
    private Long telegramUserId;
    @CreationTimestamp
    @Column(name = "first_login_date", nullable = false)
    private LocalDateTime firstLoginDate;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_state", nullable = false)
    private UserState userState;
}
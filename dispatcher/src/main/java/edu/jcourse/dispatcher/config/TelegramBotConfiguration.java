package edu.jcourse.dispatcher.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "org.telegram.telegrambots"
})
public class TelegramBotConfiguration {
}
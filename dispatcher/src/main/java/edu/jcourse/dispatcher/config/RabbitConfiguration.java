package edu.jcourse.dispatcher.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RabbitConfiguration {
    @Value("${spring.rabbitmq.queues.text-message-update}")
    private String textMessageUpdateQueue;

    @Value("${spring.rabbitmq.queues.doc-message-update}")
    private String docMessageUpdateQueue;

    @Value("${spring.rabbitmq.queues.photo-message-update}")
    private String photoMessageUpdateQueue;

    @Value("${spring.rabbitmq.queues.answer-message}")
    private String answerMessageQueue;

//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public Queue textMessageQueue() {
//        return new Queue(textMessageUpdateQueue);
//    }
//
//    @Bean
//    public Queue docMessageQueue() {
//        return new Queue(docMessageUpdateQueue);
//    }
//
//    @Bean
//    public Queue photoMessageQueue() {
//        return new Queue(photoMessageUpdateQueue);
//    }
//
//    @Bean
//    public Queue answerMessageQueue() {
//        return new Queue(answerMessageQueue);
//    }
}
package com.example.messaging;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.example.model.UserMessage;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
public class MessageConsumer {

    // Create an input binder to receive messages from `topic-two` using a Consumer
    // bean.
    @Bean
    public Consumer<Message<UserMessage>> receiveMessageFromTopic() {
        return message -> {
            log.info(
                    "Message arrived via an input binder from topic! Payload: " + message.getPayload());
            System.out.println(
                    "Message arrived via an input binder from topic! Payload: " + message.getPayload());
        };
    }
    
}



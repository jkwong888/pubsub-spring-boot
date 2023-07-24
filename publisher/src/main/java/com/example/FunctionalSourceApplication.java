/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.example.model.UserMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import reactor.core.publisher.Flux;

/**
 * Spring Boot application for running the Spring Cloud Stream source.
 *
 * <p>This class bootstraps the Spring Boot application and creates the {@link Sinks.Many} bean that
 * is used for communication between {@link FrontendController} and {@link Source}.
 *
 * @since 1.2
 */
@SpringBootApplication
public class FunctionalSourceApplication {
  private static final Log LOGGER = LogFactory.getLog(FunctionalSourceApplication.class);
  private static final Random rand = new Random(2020);

  // [START pubsub_spring_cloud_stream_output_binder]
  // Create an output binder to send messages to `topic-one` using a Supplier bean.
  @Bean
  public Supplier<Flux<Message<UserMessage>>> sendMessageToTopic() {
    return () ->
        Flux.<Message<UserMessage>>generate(
                sink -> {
                  try {
                    Thread.sleep(5000);
                  } catch (InterruptedException e) {
                    // Stop sleep earlier.
                  }

                  Message<UserMessage> message =
                      MessageBuilder.createMessage(
                        new UserMessage("message-" + rand.nextInt(1000), "me"), 
                        new MessageHeaders(Collections.emptyMap()));
                  LOGGER.info(
                      "Sending a message via the output binder to topic-one! Payload: "
                          + message.getPayload());
                  sink.next(message);
                })
            .subscribeOn(Schedulers.boundedElastic());
  }
  // [END pubsub_spring_cloud_stream_output_binder]

  public static void main(String[] args) {
    SpringApplication.run(FunctionalSourceApplication.class, args);
  }
}

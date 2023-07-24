/*
 * Copyright 2017-2019 the original author or authors.
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

package com.example.controller;

import com.example.config.PubSubProperties;
import com.example.model.UserMessage;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.AcknowledgeablePubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/** Web app for Pub/Sub sample application. */
@RestController
public class MessageRouterController {

  private static final Log LOGGER = LogFactory.getLog(MessageRouterController.class);

  private final PubSubTemplate pubSubTemplate;

  private final PubSubAdmin pubSubAdmin;
  
  @Autowired
  private PubSubProperties pubSubProperties;

  public MessageRouterController(PubSubTemplate pubSubTemplate, PubSubAdmin pubSubAdmin) {
    this.pubSubTemplate = pubSubTemplate;
    this.pubSubAdmin = pubSubAdmin;
  }

  @PostMapping("/postMessage")
  public void publish(
      @RequestHeader("X-Topic-Name") String topicName,
      @RequestBody List<UserMessage> message) {
        // TODO: check authentication?

        // TODO: use request properties to route topic: we can use the host header, etc.

        // TODO: note that "UserMessage" type is just an example.  in practice to allow flexible message schemas,
        // just publish messages as-is as strings (or provide some base schema that all messages must conform to) and allow
        // downstream consumers to deserialize
        // advantage of defining the type here is you can do some validation before it hits a downstream topic,
        // at the cost of some flexibility
        final String realTopicName = pubSubProperties.getTopics().get(topicName);
        //final TopicName pubsubTopicName = TopicName.of()
    


  }

}

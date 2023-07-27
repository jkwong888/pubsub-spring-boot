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

import com.example.model.UserMessage;
import com.google.pubsub.v1.TopicName;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Web app for Pub/Sub sample application. */
@RestController
@Slf4j
public class MessageRouterController {

  @Autowired
  private StreamBridge streamBridge;

  @Autowired
  private BindingServiceProperties bindingServiceProperties;
 

  public MessageRouterController() {
  }

  private void validateTopic() {


  }

  @RequestMapping(path = "/postMessage", method = RequestMethod.POST,  consumes = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void publish(
      @RequestHeader("X-Topic-Name") String topicName,
      @RequestBody List<UserMessage> message) {
        // TODO: check authentication?

        // TODO: use request properties to route topic: we can use the host header, etc.
        final BindingProperties bindingProps = bindingServiceProperties.getBindings().get(topicName);
        if (bindingProps == null) {
          // TODO: exception
        }

        final String realTopicName = bindingProps.getDestination();
        if(realTopicName == null) {
          // TODO: exception
        }
        final TopicName pubsubTopicName = TopicName.parse(realTopicName);

        log.info("publishing message to pubsub topic: project: {}, topic: {}", pubsubTopicName.getProject(), pubsubTopicName.getTopic());
 
        // TODO: note that "UserMessage" type is just an example.  in practice to allow flexible message schemas,
        // just publish messages as-is as strings (or provide some base schema that all messages must conform to) and allow
        // downstream consumers to deserialize
        // advantage of defining the type here is you can do some validation before it hits a downstream topic,
        // at the cost of some flexibility

        streamBridge.send(realTopicName, message);

  }

}

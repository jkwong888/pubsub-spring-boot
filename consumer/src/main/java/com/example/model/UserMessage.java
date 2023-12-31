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

package com.example.model;

import java.time.LocalDateTime;

import lombok.ToString;

/**
 * A user message for the sample app.
 *
 * <p>The Source version of {@link UserMessage} object is immutable. The data will be serialized as
 * JSON, so as long as the objects are compatible, they do not have to be the same.
 *
 * @since 1.2
 */
@ToString
public class UserMessage {

  private String body;

  private String username;

  private LocalDateTime createdAt;

  public UserMessage(String body, String username) {
    this.body = body;
    this.username = username;
    this.createdAt = LocalDateTime.now();
  }

  public String getBody() {
    return this.body;
  }

  public String getUsername() {
    return this.username;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }
}


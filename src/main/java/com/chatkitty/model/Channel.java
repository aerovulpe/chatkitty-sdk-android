/*
 * Copyright 2020 ChatKitty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chatkitty.model;

public class Channel {

  private long id;

  private String type;

  private String name;

  private Relays _relays;

  private Topics _topics;

  private Destinations _destinations;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Relays get_relays() {
    return _relays;
  }

  public void set_relays(Relays _relays) {
    this._relays = _relays;
  }

  public Topics get_topics() {
    return _topics;
  }

  public void set_topics(Topics _topics) {
    this._topics = _topics;
  }

  public Destinations get_destinations() {
    return _destinations;
  }

  public void set_destinations(Destinations _destinations) {
    this._destinations = _destinations;
  }

  public static class Topics {

    private String messages;

    public String getMessages() {
      return messages;
    }

    public void setMessages(String messages) {
      this.messages = messages;
    }
  }

  public static class Destinations {

    private String message;

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }

  public static class Relays {

    private String self;

    private String messages;

    public String getSelf() {
      return self;
    }

    public void setSelf(String self) {
      this.self = self;
    }

    public String getMessages() {
      return messages;
    }

    public void setMessages(String messages) {
      this.messages = messages;
    }
  }
}

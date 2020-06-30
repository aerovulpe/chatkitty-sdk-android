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
package com.chatkitty;

import org.jetbrains.annotations.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.chatkitty.model.CurrentUser;
import com.chatkitty.model.PagedResource;
import com.chatkitty.model.SessionNotStartedException;
import com.chatkitty.model.channel.response.GetChannelResponse;
import com.chatkitty.model.channel.response.GetChannelsResult;
import com.chatkitty.model.session.response.SessionStartResult;
import com.chatkitty.model.user.response.GetCurrentUserResult;
import com.chatkitty.stompx.stompx.StompWebSocketClient;
import com.chatkitty.stompx.stompx.StompWebSocketClientCallBack;
import com.chatkitty.stompx.stompx.WebSocketConfiguration;
import com.chatkitty.stompx.stompx.stomp.StompSubscription;
import com.chatkitty.stompx.stompx.stomp.WebSocketEvent;
import com.chatkitty.stompx.stompx.stomp.stompframe.StompServerFrame;

import okhttp3.OkHttpClient;

public class ChatKittyImpl implements ChatKitty {

  private final String apiKey;
  @Nullable private StompWebSocketClient client;

  @Nullable private SessionStartResult session;

  public ChatKittyImpl(String apiKey) {
    this.apiKey = apiKey;
  }

  @Override
  public void startSession(String username, ChatKittyCallback<SessionStartResult> callback) {
    WebSocketConfiguration configuration =
        new WebSocketConfiguration(
            apiKey, username, "https://staging-api.chatkitty.com", "/stompx");

    client = new StompWebSocketClient(new OkHttpClient(), new ObjectMapper(), configuration);
    client.start();

    client.subscribeRelay(
        "/application/users.me.relay",
        new WebSocketClientCallBack<CurrentUser>(CurrentUser.class) {
          @Override
          void onParsedMessage(
              CurrentUser resource, StompWebSocketClient client, StompSubscription subscription) {
            SessionStartResult result = new SessionStartResult();
            result.setCurrentUser(resource);
            session = result;
            callback.onSuccess(result);
          }
        });
  }

  @Override
  public void getCurrentUser(ChatKittyCallback<GetCurrentUserResult> callback) {
    if (client == null || session == null) {
      // TODO - Should add more potential scenarios here.
      callback.onError(new SessionNotStartedException());
      return;
    }
    client.subscribeRelay(
        session.getCurrentUser().get_relays().getSelf(),
        new WebSocketClientCallBack<CurrentUser>(CurrentUser.class) {
          @Override
          void onParsedMessage(
              CurrentUser resource, StompWebSocketClient client, StompSubscription subscription) {
            GetCurrentUserResult result = new GetCurrentUserResult();
            result.setCurrentUser(resource);
            callback.onSuccess(result);
          }
        });
  }

  @Override
  public void getChannels(ChatKittyCallback<GetChannelsResult> callback) {
    if (client == null || session == null) {
      // TODO - Should add more potential scenarios here.
      callback.onError(new SessionNotStartedException());
      return;
    }

    client.subscribeRelay(
        session.getCurrentUser().get_relays().getChannels(),
        new WebSocketPagedClientCallBack<GetChannelResponse>(GetChannelResponse.class) {
          @Override
          void onParsedMessage(
              PagedResource<GetChannelResponse> resource,
              StompWebSocketClient client,
              StompSubscription subscription) {
            GetChannelsResult result =
                new GetChannelsResult(
                    resource.get_embedded().getChannels(), resource.get_relays().getNext());
            callback.onSuccess(result);
          }
        });
  }

  private abstract static class WebSocketPagedClientCallBack<T>
      implements StompWebSocketClientCallBack {

    private final ObjectMapper objectMapper =
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final Class<T> type;

    WebSocketPagedClientCallBack(Class<T> type) {
      this.type = type;
    }

    @Override
    public void onNewMessage(
        StompWebSocketClient client, StompServerFrame frame, StompSubscription subscription) {
      try {
        JavaType pagedType =
            objectMapper.getTypeFactory().constructParametricType(PagedResource.class, type);

        JavaType javaType =
            objectMapper.getTypeFactory().constructParametricType(WebSocketEvent.class, pagedType);

        WebSocketEvent<PagedResource<T>> response = objectMapper.readValue(frame.body, javaType);
        onParsedMessage(response.getResource(), client, subscription);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }

    abstract void onParsedMessage(
        PagedResource<T> resource, StompWebSocketClient client, StompSubscription subscription);
  }

  private abstract static class WebSocketClientCallBack<T> implements StompWebSocketClientCallBack {

    private final ObjectMapper objectMapper =
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final Class<T> type;

    WebSocketClientCallBack(Class<T> type) {
      this.type = type;
    }

    @Override
    public void onNewMessage(
        StompWebSocketClient client, StompServerFrame frame, StompSubscription subscription) {
      try {
        JavaType javaType =
            objectMapper.getTypeFactory().constructParametricType(WebSocketEvent.class, type);
        WebSocketEvent<T> response = objectMapper.readValue(frame.body, javaType);
        onParsedMessage(response.getResource(), client, subscription);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }

    abstract void onParsedMessage(
        T resource, StompWebSocketClient client, StompSubscription subscription);
  }
}

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

import com.chatkitty.listeners.channel.ChannelEventListener;
import com.chatkitty.listeners.channel.ChannelEventListenerRegistration;
import com.chatkitty.model.Channel;
import com.chatkitty.model.channel.response.GetChannelsResult;
import com.chatkitty.model.message.response.GetMessagesResult;
import com.chatkitty.model.session.response.SessionStartResult;
import com.chatkitty.model.user.response.GetCurrentUserResult;

/** ChatKitty facade */
public interface ChatKitty {

  static ChatKitty getInstance(String apiKey) {
    return new ChatKittyImpl(apiKey);
  }

  void startSession(String username, ChatKittyCallback<SessionStartResult> callback);

  void getCurrentUser(ChatKittyCallback<GetCurrentUserResult> callback);

  void getChannels(ChatKittyCallback<GetChannelsResult> callback);

  void getChannelMessages(Channel channel, ChatKittyCallback<GetMessagesResult> callback);

  ChannelEventListenerRegistration registerChannelEventListener(
      Channel channel, ChannelEventListener listener);
}

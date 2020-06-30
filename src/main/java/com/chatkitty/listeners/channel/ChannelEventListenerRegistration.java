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
package com.chatkitty.listeners.channel;

import org.jetbrains.annotations.Nullable;

public class ChannelEventListenerRegistration {

  @Nullable private final Subscribe subscribe;

  public interface Subscribe {

    void onDeregister();
  }

  private ChannelEventListenerRegistration(@Nullable Subscribe subscribe) {
    this.subscribe = subscribe;
  }

  public static ChannelEventListenerRegistration create() {
    return new ChannelEventListenerRegistration(null);
  }

  public static ChannelEventListenerRegistration create(Subscribe subscribe) {
    return new ChannelEventListenerRegistration(subscribe);
  }

  public void deregister() {
    if (subscribe != null) {
      subscribe.onDeregister();
    }
  }
}

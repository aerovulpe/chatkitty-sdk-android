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
package com.chatkitty.model.message.response;

import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.chatkitty.model.message.Message;
import com.chatkitty.pagination.PageIterator;

public final class GetMessagesResult {

  private final Iterator<Message> iterator;

  @Nullable private String nextDestination;

  public GetMessagesResult(List<Message> messages, @Nullable String nextDestination) {
    this.iterator = messages.iterator();
    this.nextDestination = nextDestination;
  }

  public PageIterator<Message> iterator() {
    return new PageIterator<Message>() {
      @Override
      public boolean hasNext() {
        if (!iterator.hasNext() && nextDestination != null) {
          // TODO - Call pagination socket.
        }
        return iterator.hasNext();
      }

      @Override
      public Message next() {
        return iterator.next();
      }
    };
  }
}

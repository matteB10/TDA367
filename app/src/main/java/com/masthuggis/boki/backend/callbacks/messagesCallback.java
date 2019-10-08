package com.masthuggis.boki.backend.callbacks;

import com.masthuggis.boki.model.iMessage;

import java.util.List;

@FunctionalInterface
public interface messagesCallback {
    void onCallback(List<iMessage> messagesList);
}

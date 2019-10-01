package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.iChat;

import java.util.List;

@FunctionalInterface
public interface chatCallback {
    void onCallback(List<iChat> chatsList);
}

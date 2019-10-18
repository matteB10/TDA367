package com.masthuggis.boki.model.observers;

public interface BackendObserver {
    void onMessagesChanged();
    void onChatsChanged();
}

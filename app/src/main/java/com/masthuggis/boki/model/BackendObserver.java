package com.masthuggis.boki.model;

public interface BackendObserver {
    void onMessagesChanged();
    void onAdvertisementsChanged();
    void onChatsChanged();
}

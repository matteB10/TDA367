package com.masthuggis.boki.model.observers;

/**
 * An interface implemented by classes which want to observe updates done to
 * the chats in the application.
 *
 * Implemented by ChatPresenter and BackendDataHandler, BackendReader and BackendWriter
 * holds a list of observers.
 * Written by masthuggis
 */
public interface ChatObserver {
    void onChatUpdated();


}

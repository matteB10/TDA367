package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.model.observers.MessagesObserver;

import java.util.List;

public class BackendFactory {
    /**
     * Factory responsible for creating objects in the backend-package. Used to return the same iBackend for consistency without
     * making the iBackend a singleton.
     * Used by DataModel and BackendDataHandler.
      * Written by masthuggis
     */

    private static iBackend backend;

    public static iBackend createBackend() {
        if (backend == null) {
            backend = new BackendDataHandler();
        }
            return backend;
        }

        static BackendReader createBackendReader(List<ChatObserver> chatObservers, List<MessagesObserver> messagesObservers) {
            return new BackendReader(chatObservers,messagesObservers);
        }

        static BackendWriter createBackendWriter(List<ChatObserver> chatObservers) {
            return new BackendWriter(chatObservers);
        }
    }

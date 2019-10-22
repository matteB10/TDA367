package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.model.observers.MessagesObserver;

import java.util.List;

public class BackendFactory {
    /**
     * Factory for creating iBackend. Used to return the same backend for consistency without making the backend a singleton.
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

        public static BackendWriter createBackendWriter(List<ChatObserver> chatObservers, List<MessagesObserver> messagesObservers) {
            return new BackendWriter(chatObservers,messagesObservers);
        }
    }

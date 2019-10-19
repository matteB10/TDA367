package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.observers.BackendObserver;

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

        public static BackendReader createBackendReader(List<BackendObserver> backendObservers) {
            return new BackendReader(backendObservers);
        }

        public static BackendWriter createBackendWriter(List<BackendObserver> backendObservers) {
            return new BackendWriter(backendObservers);
        }
    }

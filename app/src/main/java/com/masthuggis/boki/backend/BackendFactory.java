package com.masthuggis.boki.backend;

public class BackendFactory {
    /**
     * Factory for creating iBackend. Used to return the same backend for consistency without making the backend a singleton.
     */

    private static iBackend backend;

    public static iBackend createBackend() {
        if (backend == null) {
            backend= new BackendDataHandler();
        }
            return backend;
        }
    }

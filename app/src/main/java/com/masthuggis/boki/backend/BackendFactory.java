package com.masthuggis.boki.backend;

public class BackendFactory {

    private static iBackend backend;

    public static iBackend createBackend() {
        if (backend == null) {
            backend= new BackendDataHandler();
        }
            return backend;
        }
    }

package com.masthuggis.boki.backend.callbacks;

@FunctionalInterface
public interface UrlCallback {
    void onCallback(String url);
}

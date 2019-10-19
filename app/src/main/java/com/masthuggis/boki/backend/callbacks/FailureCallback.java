package com.masthuggis.boki.backend.callbacks;

@FunctionalInterface
public interface FailureCallback {
    void onFailure(String errorMessage);
}

package com.masthuggis.boki.model.callbacks;

@FunctionalInterface
public interface FailureCallback {
    void onFailure(String errorMessage);
}

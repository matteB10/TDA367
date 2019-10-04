package com.masthuggis.boki.backend;

import androidx.annotation.Nullable;

@FunctionalInterface
public interface FailureCallback {
    void onFailure(@Nullable String errorMessage);
}

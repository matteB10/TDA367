package com.masthuggis.boki.backend.callbacks;

import androidx.annotation.Nullable;

@FunctionalInterface
public interface FailureCallback {
    void onFailure(@Nullable String errorMessage);
}

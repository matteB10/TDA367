package com.masthuggis.boki.backend.callbacks;

import java.io.File;

@FunctionalInterface
public interface FileCallback {
    void onCallback(File file);
}

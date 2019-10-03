package com.masthuggis.boki.backend;

import java.io.File;

@FunctionalInterface
public interface FileCallback {
    void onCallback(File file);
}

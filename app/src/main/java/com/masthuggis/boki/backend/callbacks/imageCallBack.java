package com.masthuggis.boki.backend.callbacks;

import java.net.URI;

@FunctionalInterface
public interface imageCallBack {
    void onCallBack(URI imageURI);
}

package com.masthuggis.boki.backend;

import java.net.URI;

@FunctionalInterface
public interface imageCallBack {
    void onCallBack(URI imageURI);
}

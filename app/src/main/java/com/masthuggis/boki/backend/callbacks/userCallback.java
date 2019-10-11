package com.masthuggis.boki.backend.callbacks;

import com.masthuggis.boki.model.iUser;

@FunctionalInterface
public interface userCallback {
    void onCallback(iUser newUser);
}

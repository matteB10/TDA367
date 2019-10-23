package com.masthuggis.boki.model.callbacks;

import com.masthuggis.boki.model.iUser;

@FunctionalInterface
public interface userCallback {
    void onCallback(iUser newUser);
}

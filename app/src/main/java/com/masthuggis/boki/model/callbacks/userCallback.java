package com.masthuggis.boki.model.callbacks;

import com.masthuggis.boki.model.iUser;
/**
 * A functional interface which only contains one method. Is used to
 * control the flow of the application and extract data in the form of an object
 * of the iUser type.
 * Used by DataModel
 * Written by masthuggis
 */
@FunctionalInterface
public interface userCallback {
    void onCallback(iUser newUser);
}

package com.masthuggis.boki.model.callbacks;

@FunctionalInterface
/**
 * A functional interface which only contains one method. Is used to
 * control the flow of the application and convey whether or not a
 * database call was successful or not.
 * Used by SignUpPresenter,UserRepository, BackendReader and BackendWriter.
 * Written by masthuggis
 */
public interface SuccessCallback {
    void onSuccess();
}

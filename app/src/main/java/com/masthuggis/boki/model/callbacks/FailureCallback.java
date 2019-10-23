package com.masthuggis.boki.model.callbacks;

@FunctionalInterface

/**
 * A functional interface which only contains one method. Is used to
 * control the flow of the application and convey whether a read from
 * the database was successful or not.
 * Used by SignUpPresenter and UserRepository.
 * Written by masthuggis
 */
public interface FailureCallback {
    void onFailure(String errorMessage);
}

package com.masthuggis.boki.model;

/**
 * Exception used when an action is requested when the user needs to be logged in.
 */
public class UserNotLoggedInException extends Exception {
    public UserNotLoggedInException() {
    }

    public UserNotLoggedInException(String message) {
        super(message);
    }
}

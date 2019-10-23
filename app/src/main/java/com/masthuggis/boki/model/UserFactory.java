package com.masthuggis.boki.model;

/**
 * Used by UserRepository.
 * Written by masthuggis
 */
public class UserFactory {

    public static User createUser(String email, String displayName, String userID) {
        return new User(email, displayName, userID);
    }
}

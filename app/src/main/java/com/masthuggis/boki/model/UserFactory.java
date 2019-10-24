package com.masthuggis.boki.model;

/**
 * Creates of the User-type is created through this Factory.
 * Returns static type iUser.
 * Used by UserRepository.
 * Written by masthuggis
 */
public class UserFactory {

    public static iUser createUser(String email, String displayName, String userID) {
        return new User(email, displayName, userID);
    }
}

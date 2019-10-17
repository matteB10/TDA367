package com.masthuggis.boki.model;

public class UserFactory {

    public static User createUser(String email, String displayName, String userID) {
        return new User(email, displayName, userID);
    }
}

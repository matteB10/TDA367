package com.masthuggis.boki.model;

public class UserFactory {

    public static User createUser(String email, String displayname, String userID, DataModel dataModel) {
        return new User(email, displayname, userID, dataModel);
    }
}

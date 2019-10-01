package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.User;

public class UserFactory {

    public static User createUser(String email, String displayname, String userID){
        return new User(email,displayname,userID);
    }
}

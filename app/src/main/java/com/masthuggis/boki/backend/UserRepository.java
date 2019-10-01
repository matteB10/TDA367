package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.iUser;

import java.util.Map;

public class UserRepository {
    private static UserRepository userRepository;
    private iUser user;

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }


    public void signIn(String email, String password) {
        BackendDataHandler.getInstance().userSignIn(email, password);
        loggedIn();
    }

    public void signUp(String email, String password) {
        BackendDataHandler.getInstance().userSignUp(email, password);
        loggedOut();
    }

    private void loggedIn() {
        Map<String, String> map = BackendDataHandler.getInstance().getUser();
        user= UserFactory.createUser(map.get("email"), map.get("displayname"), map.get("userID"));
    }
    private void loggedOut(){
        user=null;

    }



    public iUser getCurrentUser() {
        return user;
    }
}


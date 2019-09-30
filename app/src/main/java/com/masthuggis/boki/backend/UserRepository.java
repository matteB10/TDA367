package com.masthuggis.boki.backend;

public class UserRepository {
    private static UserRepository userRepository;

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }


    public void signIn(String email, String password) {
        BackendDataFetcher.getInstance().userSignIn(email,password);
    }

    public void signUp(String email, String password) {
        BackendDataFetcher.getInstance().userSignUp(email,password);

    }
}


package com.masthuggis.boki.backend;

public class UserRepository {
    private static UserRepository userRepository;

    /**
     * @return
     */
    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    /** Method to sign a user in using a email address and a chosen password.
     * @param email
     * @param password
     */
    public void signIn(String email, String password) {
        BackendDataFetcher.getInstance().userSignIn(email,password);
    }

    /**Method to sign up a user with a email address and a chosen password,
     * connected to Firebase.
     * @param email
     * @param password
     */
    public void signUp(String email, String password) {
        BackendDataFetcher.getInstance().userSignUp(email,password);
    }

    /**Checks if a user is sign in to the app.
     * @return true if a user is signed in
     */
    public boolean isSignedIn() {
        return BackendDataFetcher.getInstance().isUserSignedIn();
    }

}


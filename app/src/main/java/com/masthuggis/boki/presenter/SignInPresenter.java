package com.masthuggis.boki.presenter;
import android.app.Activity;

import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.model.User;

public class SignInPresenter {
    private UserRepository repo;
    private View view;
    private User user;

    public SignInPresenter(View view){
        this.view = view;
        this.repo = UserRepository.getInstance();

    }


    public void onSignInButtonPressed(String email, String password, Activity activity) {
        repo.signIn(email, password,activity);
    }

    public void onSignUpButtonPressed() {
        view.showSignUpScreen();
    }


    public interface View {
        void showSignUpScreen();
    }
}

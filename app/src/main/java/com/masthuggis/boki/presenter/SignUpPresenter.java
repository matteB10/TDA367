package com.masthuggis.boki.presenter;

import android.app.Activity;
import android.app.ProgressDialog;

import com.masthuggis.boki.backend.UserRepository;

public class SignUpPresenter {

    private UserRepository repo = UserRepository.getInstance();
    private View view;

    public ProgressDialog PD;

    public SignUpPresenter (View view){
        this.view = view;
    }

    public void onSignInButtonPressed(){
        view.showSignInScreen();
    }

    public void onSignUpButtonPressed(String email,String password,Activity activity) {
        repo.signUp(email, password, activity);
    }

    public interface View {
        void showSignInScreen();
    }
}
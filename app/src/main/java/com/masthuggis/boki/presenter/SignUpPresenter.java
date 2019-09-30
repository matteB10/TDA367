package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.UserRepository;

public class SignUpPresenter {

    private UserRepository repo = UserRepository.getInstance();
    private View view;

    public SignUpPresenter (View view){
        this.view = view;
    }

    public void onSignInButtonPressed(){
        view.showSignInScreen();
    }

    public void onSignUpButtonPressed(String email,String password) {
        repo.signUp(email, password);
    }

    public interface View {
        void showSignInScreen();
    }
}
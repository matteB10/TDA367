package com.masthuggis.boki.presenter;

public class SignUpPresenter {
    private View view;

    public SignUpPresenter (View view){
        this.view = view;
    }

    public void onSignInButtonPressed(){
        view.showSignInScreen();
    }

    public void onSignUpButtonPressed() {
        //TODO Firebase: register user when user press this btn
    }

    public interface View {
        void showSignInScreen();
    }
}
package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.model.DataModel;

public class SignUpPresenter {

    private UserRepository repo = UserRepository.getInstance();
    private View view;

    public SignUpPresenter (View view){
        this.view = view;
    }

    public void onSignInButtonPressed(){
        view.showSignInScreen();
    }

    public void onSignUpButtonPressed(String email, String password, String username) {
        DataModel.getInstance().signUp(email,password, this::onSignUpSuccess);
    }

    private void onSignUpSuccess() {
        view.signedIn();
    }

    public interface View {
        void showSignInScreen();

        void signedIn();
    }
}
package com.masthuggis.boki.presenter;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.User;

public class SignInPresenter {
    private Repository repo;
    private View view;
    private User user;

    public SignInPresenter(View view){
        this.view = view;
        this.repo = Repository.getInstance();

    }


    public void onSignInButtonPressed() {
        //TODO Firebase: login when user press this btn
    }

    public void onSignUpButtonPressed() {
        view.showSignUpScreen();
    }



    public interface View {
        void showSignUpScreen();
    }
}

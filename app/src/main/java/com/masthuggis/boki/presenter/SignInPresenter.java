package com.masthuggis.boki.presenter;
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


    public void onSignInButtonPressed(String email, String password) {
        repo.signIn(email, password);
        view.showProfileScreen();
    }

    public void onSignUpButtonPressed() {
        view.showSignUpScreen();
    }


    public interface View {
        void showSignUpScreen();
        void showProfileScreen();
    }
}

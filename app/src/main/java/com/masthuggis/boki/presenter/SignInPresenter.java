package com.masthuggis.boki.presenter;
import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.model.User;

public class SignInPresenter {
    private UserRepository repo;
    private View view;

    public SignInPresenter(View view){
        this.view = view;
        this.repo = UserRepository.getInstance();

    }

    public void onSignInButtonPressed(String email, String password) {
        repo.signIn(email, password, this::onSignInSuccess, this::onSignInFailed);
    }

    private void onSignInSuccess() {
        view.showProfileScreen();
    }

    private void onSignInFailed() {
        view.showSignInFailedMessage();
    }

    public void onSignUpButtonPressed() {
        view.showSignUpScreen();
    }

    public interface View {
        void showSignUpScreen();
        void showProfileScreen();
        void showSignInFailedMessage();
    }
}

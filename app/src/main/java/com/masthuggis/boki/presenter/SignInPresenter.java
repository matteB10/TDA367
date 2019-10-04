package com.masthuggis.boki.presenter;
import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.model.User;
import com.masthuggis.boki.utils.FormHelper;

public class SignInPresenter {
    private UserRepository repo;
    private View view;

    public SignInPresenter(View view){
        this.view = view;
        this.repo = UserRepository.getInstance();

    }

    public void onSignInButtonPressed(String email, String password) {
        if (fieldsAreBadlyFormatted(email, password)) {
            view.showSignInFailedMessage("Felaktig inmatning. Skrev du verkligen rätt?");
            return;
        }
        repo.signIn(email, password, this::onSignInSuccess, errorMessage -> onSignInFailed(errorMessage));
    }

    private boolean fieldsAreBadlyFormatted(String email, String password) {
        FormHelper fh = FormHelper.getInstance();
        return !fh.isValidEmail(email) || email.isEmpty() || password.isEmpty();
    }

    private void onSignInSuccess() {
        view.showProfileScreen();
    }

    private void onSignInFailed(String errorMessage) {
        view.showSignInFailedMessage(errorMessage);
    }

    public void onSignUpButtonPressed() {
        view.showSignUpScreen();
    }

    public interface View {
        void showSignUpScreen();
        void showProfileScreen();
        void showSignInFailedMessage(String errorMessage);
    }
}

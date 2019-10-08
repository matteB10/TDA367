package com.masthuggis.boki.presenter;

import androidx.annotation.Nullable;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.FormHelper;

public class SignUpPresenter {
    private View view;

    public SignUpPresenter(View view) {
        this.view = view;
    }

    public void onSignInButtonPressed() {
        view.showSignInScreen();
    }

    public void onSignUpButtonPressed(String email, String password, String username) {
        if (anyFieldIsBadlyFormatted(email, password, username)) {
            view.showSignUpFailedMessage("Felaktig inmatning. Skrev du verkligen rätt?");
            return;
        }
        DataModel.getInstance().signUp(email, password, username, this::onSignUpSuccess, (errorMessage -> onSignUpFailed(errorMessage)));
    }

    private boolean anyFieldIsBadlyFormatted(String email, String password, String username) {
        FormHelper fh = FormHelper.getInstance();
        return !fh.isValidEmail(email) || email.isEmpty() || password.isEmpty() || username.isEmpty();
    }

    private void onSignUpSuccess() {
        view.signedIn();
    }

    private void onSignUpFailed(@Nullable String errorMessage) {
        view.showSignUpFailedMessage(errorMessage);
    }

    public interface View {
        void showSignInScreen();
        void showSignUpFailedMessage(String message);
        void signedIn();
    }
}
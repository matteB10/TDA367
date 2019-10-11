package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.FormHelper;

public class SignInPresenter {
    private View view;
    private DataModel dataModel;
    public SignInPresenter(View view, DataModel dataModel) {
        this.dataModel=dataModel;
        this.view = view;

    }

    public void onSignInButtonPressed(String email, String password) {
        if (anyFieldIsBadlyFormatted(email, password)) {
            view.showSignInFailedMessage("Felaktig inmatning. Skrev du verkligen rätt?");
            return;
        }
        dataModel.SignIn(email, password, this::onSignInSuccess, errorMessage -> onSignInFailed(errorMessage));
    }

    private boolean anyFieldIsBadlyFormatted(String email, String password) {
        FormHelper fh = FormHelper.getInstance();
        return !fh.isValidEmail(email) || email.isEmpty() || password.isEmpty();
    }

    private void onSignInSuccess() {
        view.signInSuccess();
    }

    private void onSignInFailed(String errorMessage) {
        view.showSignInFailedMessage(errorMessage);
    }

    public void onSignUpButtonPressed() {
        view.showSignUpScreen();
    }

    public interface View {
        void showSignUpScreen();
        void signInSuccess();
        void showSignInFailedMessage(String errorMessage);
    }
}

package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.view.ValidatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by SignInActivity
 * Written by masthuggis
 *
 *
 */
public class SignInPresenter<T extends ValidatorView & SignInPresenter.View> {
    private ValidatorPresenter validator;
    private T view;
    private DataModel dataModel;

    public SignInPresenter(T view, DataModel dataModel) {
        this.dataModel = dataModel;
        this.view = view;
        validator = new ValidatorPresenter(view);
    }

    public void onSignInButtonPressed(String email, String password) {
        List<String> dataToBeProcessed = new ArrayList<>();
        dataToBeProcessed.add(email);
        dataToBeProcessed.add(password);

        if (validator.onAccessRequested(dataToBeProcessed, "Felaktig inmatning. Skrev du verkligen rätt?")) {
            dataModel.signIn(email, password, this.view::accessGranted, error -> view.showAccessFailedMessage(error));
        }
    }

    public void onSignUpButtonPressed() {
        view.showSignUpScreen();
    }

    public interface View {
        void showSignUpScreen();
    }
}

package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.view.ValidatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter dealing the sign up logic.
 * @param <T>
 */
public class SignUpPresenter<T extends ValidatorView & SignUpPresenter.View> {
    private ValidatorPresenter validator;
    private T view;
    private DataModel dataModel;

    public SignUpPresenter(T view, DataModel dataModel) {
        this.dataModel = dataModel;
        this.view = view;
        validator = new ValidatorPresenter(view);
    }

    public void onSignInButtonPressed() {
        view.showSignInScreen();
    }

    /**
     * Asks validator to process data. If it is valid a request is made to the database. If it
     * is valid view is notified and if not an error message will be shown.
     * @param email
     * @param password
     * @param username
     */
    public void onSignUpButtonPressed(String email, String password, String username) {
        List<String> dataToBeProcessed = new ArrayList<>();
        dataToBeProcessed.add(email);
        dataToBeProcessed.add(password);
        dataToBeProcessed.add(username);

        if (validator.onAccessRequested(dataToBeProcessed, "Felaktig inmatning. Skrev du verkligen rätt?")) {
            dataModel.signUp(email, password, username, this.view::accessGranted, error -> view.showAccessFailedMessage(error));
        }
    }

    public interface View {
        void showSignInScreen();
    }
}
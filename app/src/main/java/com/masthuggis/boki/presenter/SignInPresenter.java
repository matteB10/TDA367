package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.FailureCallback;
import com.masthuggis.boki.backend.SuccessCallback;
import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.User;

public class SignInPresenter {
    private UserRepository repo;
    private View view;
    private User user;

    public SignInPresenter(View view) {
        this.view = view;
        this.repo = UserRepository.getInstance();

    }


    public void onSignInButtonPressed(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        DataModel.getInstance().SignIn(email, password, new SuccessCallback() {
            @Override
            public void onSuccess() {
                view.showProfileScreen();
                successCallback.onSuccess();
            }
        }, new FailureCallback() {
            @Override
            public void onFailure() {
                failureCallback.onFailure();
            }
        });
    }

    public void onSignUpButtonPressed() {
        view.showSignUpScreen();
    }


    public interface View {
        void showSignUpScreen();

        void showProfileScreen();
    }
}

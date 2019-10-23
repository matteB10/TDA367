package com.masthuggis.boki.presenter;

import com.masthuggis.boki.view.ValidatorView;

import java.util.List;

/**
 *
 * Used by SignInPresenter and SignUpPresenter.
 * Written by masthuggis
 */
class ValidatorPresenter {
    private ValidatorView view;

    ValidatorPresenter(ValidatorView view) {
        this.view = view;
    }

    boolean onAccessRequested(List<String> dataToBeValidated, String errorMessage)  {
        for (String data: dataToBeValidated) {
            if (data == null) {
                accessFailed(errorMessage);
                return false;
            }
            if (data.isEmpty()) {
                accessFailed(errorMessage);
                return false;
            }
        }
        return true;
    }

    private void accessFailed(String errorMessage) {
        view.showAccessFailedMessage(errorMessage);
    }
}

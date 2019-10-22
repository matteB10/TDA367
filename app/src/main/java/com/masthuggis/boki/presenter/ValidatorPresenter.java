package com.masthuggis.boki.presenter;

import com.masthuggis.boki.view.ValidatorView;

import java.util.List;

 class ValidatorPresenter {
    private ValidatorView view;

     ValidatorPresenter(ValidatorView view) {
        this.view = view;
    }

     boolean onAccessRequested(List<String> dataToBeValidated, String errorMessage)  {
        for (String data: dataToBeValidated) {
            if (data.isEmpty() || data == null) {
                view.showAccessFailedMessage(errorMessage);
                return false;
            }
        }
        return true;
    }
}

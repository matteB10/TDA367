package com.masthuggis.boki.view;
/**
 *
 * Used by SignInActivity and SignInPresenter.
 * Written by masthuggis
 */
public interface ValidatorView {
    void showAccessFailedMessage(String message);
    void accessGranted();
}

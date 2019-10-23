package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.view.SignInActivity;
import com.masthuggis.boki.view.SignUpActivity;
import com.masthuggis.boki.view.ValidatorView;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

public class ValidatorPresentersTest {
    @Mock private ValidatorView viewMock;
    @Mock private DataModel dataMock;
    private SignInPresenter signInPresenter;
    private SignUpPresenter signUpPresenter;
    @Mock SignUpActivity signUpActivityMock;
    @Mock SignInActivity signInActivity;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    private void initSignUpPresenter() {
        signUpPresenter = new SignUpPresenter(viewMock, dataMock);
    }

    private void initSignInPresenter() {
        signInPresenter = new SignInPresenter(viewMock, dataMock);
    }

    @Test
    public void whenBadInputIsGivenUserIsNotSignedUp() {
        initSignUpPresenter();

        signUpPresenter.onSignUpButtonPressed("", "123123", "username");

        verify(viewMock).showAccessFailedMessage(any());
    }

    @Test
    public void whenGoodInputIsGivenUserIsSignedUp() {
        initSignUpPresenter();
        String email = "valid@gmail.com";
        String password = "123123";
        String username = "username";

        signUpPresenter.onSignUpButtonPressed(email, password, username);

        verify(signUpActivityMock, times(0)).accessGranted();
        verify(dataMock).signUp(anyString(), anyString(), anyString(), any(), any());
    }

    @Test
    public void whenBadInputIsGivenUserIsNotSignedIn() {
        initSignInPresenter();

        signInPresenter.onSignInButtonPressed("", "password");

        verify(viewMock).showAccessFailedMessage(any());
    }

    @Test
    public void whenGoodInputIsGivenUserIsSignedIn() {
        initSignInPresenter();

        signInPresenter.onSignInButtonPressed("valid@email.com", "password");

        verify(signInActivity, times(0)).accessGranted();
        verify(dataMock).signIn(anyString(), anyString(), any(), any());
    }
}

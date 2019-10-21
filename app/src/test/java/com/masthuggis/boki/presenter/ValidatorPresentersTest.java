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

    @Test
    public void whenBadInputIsGivenUserIsNotSignedUp() {
        signUpPresenter = new SignUpPresenter(viewMock, dataMock);

        signUpPresenter.onSignUpButtonPressed("", "123123", "username");

        verify(viewMock).showAccessFailedMessage(any());
    }

    @Test
    public void whenGoodInputIsGivenUserIsSignedUp() {
        signUpPresenter = new SignUpPresenter(signUpActivityMock, dataMock);
        String email = "valid@gmail.com";
        String password = "123123";
        String username = "username";

        signUpPresenter.onSignUpButtonPressed(email, password, username);

        verify(signUpActivityMock, times(0)).accessGranted();
        verify(dataMock).signUp(anyString(), anyString(), anyString(), any(), any());
    }

    @Test
    public void whenBadInputIsGivenUserIsNotSignedIn() {
        signInPresenter = new SignInPresenter(viewMock, dataMock);

        signInPresenter.onSignInButtonPressed("", "password");

        verify(viewMock).showAccessFailedMessage(any());
    }

    @Test
    public void whenGoodInputIsGivenUserIsSignedIn() {
        signInPresenter = new SignInPresenter(signInActivity, dataMock);

        signInPresenter.onSignInButtonPressed("valid@email.com", "password");

        verify(signInActivity, times(0)).accessGranted();
        verify(dataMock).signIn(anyString(), anyString(), any(), any());
    }
}

package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

public class SignUpPresenterTest {
    private SignUpPresenter presenter;
    @Mock
    private SignUpPresenter.View viewMock;
    @Mock
    private DataModel dataMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private void initPresenter() {
        presenter = new SignUpPresenter(viewMock, dataMock);
    }

    @Test
    public void whenBadInputIsGivenUserIsNotSignedUp() {
        initPresenter();

        presenter.onSignUpButtonPressed("", "123123", "username");

        verify(viewMock).showSignUpFailedMessage(any());
    }

    @Test
    public void whenGoodInputIsGivenUserIsSignedUp() {
        initPresenter();
        String email = "valid@gmail.com";
        String password = "123123";
        String username = "username";

        presenter.onSignUpButtonPressed(email, password, username);

        verify(viewMock, times(0)).signedIn();
        verify(dataMock).signUp(anyString(), anyString(), anyString(), any(), any());
    }
}

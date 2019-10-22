package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.view.ProfileFragment;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


public class ProfilePresenterTest {

    @Mock
    private ProfileFragment fragmentMock;
    @Mock
    private DataModel databaseMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    private ProfilePresenter presenter;

    @Test
    public void whenSignOutButtonIsPressedUserIsLoggedOutAndViewIsNotified() {
        presenter = new ProfilePresenter(fragmentMock, databaseMock);

        presenter.onSignOutPressed();

        Mockito.verify(fragmentMock).showLoginScreen();
        Mockito.verify(databaseMock).signOut();
    }
}

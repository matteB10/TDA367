package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.callbacks.SuccessCallback;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.view.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

public class MainPresenterTest {
    @Mock
    private MainActivity activityMock;
    @Mock
    private DataModel databaseMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private MainPresenter presenter;

    private void userIsLoggedIn(boolean loggedIn) {
        Mockito.when(databaseMock.isLoggedIn()).thenReturn(loggedIn);
    }

    private void initPresenter() {
        presenter = new MainPresenter(activityMock, databaseMock);
    }

    private void setupSuccessfullLogin() {
        Mockito.doAnswer(invocation -> {
            SuccessCallback callback = (SuccessCallback) invocation.getArguments()[0];
            callback.onSuccess();
            return null;
        }).when(databaseMock).initUser(any(SuccessCallback.class));
    }

    @Test
    public void loginScreenIsShownWhenUserHasNotSignedInPreviously() {
        userIsLoggedIn(false);
        initPresenter();

        presenter.init(false);

        verify(activityMock).showSignInScreen();
    }

    @Test
    public void mainScreenIsShownWhenUserHasSignedInPreviously() {
        userIsLoggedIn(true);
        setupSuccessfullLogin();
        initPresenter();

        presenter.init(false);

        verify(activityMock).showMainScreen();
    }

    @Test
    public void shouldNavigateToFavoriteScreenWhenRequested() {
        userIsLoggedIn(true);
        setupSuccessfullLogin();
        initPresenter();

        presenter.init(true);

        verify(activityMock).showFavouritesScreen();
    }
}

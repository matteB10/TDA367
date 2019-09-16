package com.masthuggis.boki;

import com.masthuggis.boki.presenter.HomePresenter;

import org.junit.Test;
import static org.junit.Assert.*;

public class HomeFragmentTest {
    @Test
    public void numAdvertsShow_IsCorrect() {
        HomePresenter.View view = createHomeView();
        HomePresenter presenter = new HomePresenter(view);
    }

    private HomePresenter.View createHomeView() {
        return new HomePresenter.View() {
            @Override
            public void showLoadingScreen() {

            }

            @Override
            public void hideLoadingScreen() {

            }
        };
    }
}

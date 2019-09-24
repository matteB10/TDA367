package com.masthuggis.boki;

import androidx.test.runner.AndroidJUnit4;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.presenter.HomePresenter;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {
    @Test
    public void numAdvertsShow_IsSameAsRepository() {
        HomePresenter.View view = createHomeView();
        HomePresenter presenter = new HomePresenter(view);

        int numItems = Repository.getInstance().getAllAds().size();

        assertEquals(presenter.getItemCount(), numItems);
    }

    private HomePresenter.View createHomeView() {
        return new HomePresenter.View() {
            @Override
            public void showLoadingScreen() {

            }

            @Override
            public void showThumbnails() {

            }

            @Override
            public void hideLoadingScreen() {

            }

            @Override
            public void showDetailsScreen(String id) {

            }

        };
    }
}

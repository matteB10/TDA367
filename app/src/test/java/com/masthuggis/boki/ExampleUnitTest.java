package com.masthuggis.boki;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.iRepository;
import com.masthuggis.boki.presenter.HomePresenter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void numAdvertsShow_IsSameAsRepository() {
        /*
        HomePresenter.View view = createHomeView();
        iRepository repository = Repository.getInstance();

        HomePresenter presenter = new HomePresenter(view, repository);
        int numItems = repository.getAllAds().size();

        assertEquals(presenter.getNumRows(), numItems);

         */
    }

    private HomePresenter.View createHomeView() {
        return new HomePresenter.View() {
            @Override
            public void showLoadingScreen() {

            }

            @Override
            public void hideLoadingScreen() {

            }

            @Override
            public void showDetailsScreen(long id) {

            }
        };
    }
}
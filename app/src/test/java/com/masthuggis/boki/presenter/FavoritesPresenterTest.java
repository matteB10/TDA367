package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.view.ListView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class FavoritesPresenterTest {
    private List<Advertisement> testData = new ArrayList<>();

    @Mock
    private ListView fragmentMock;
    @Mock
    private DataModel databaseMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    private FavoritesPresenter presenter;

    private void createTestData() {
        testData.add(AdFactory.createAd( "22/10/19:13:16:00", "UniqueOwnerID", "UniqueAdID", "ABC","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd( "22/10/19:10:16:00", "UniqueOwnerID", "UniqueAdID", "QYZ","", 490, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd( "22/10/19:10:15:00", "UniqueOwnerID", "UniqueAdID", "DEF","", 299, Condition.GOOD,"", new ArrayList<>(),null));
    }

    @Test
    public void theLatestPublishedFavoritesShouldBeDisplayedFirst() {
        createTestData();
        Mockito.when(databaseMock.getUserFavourites()).thenReturn(testData);
        presenter = new FavoritesPresenter(fragmentMock, databaseMock);

        presenter.updateData();
        List<Advertisement> adverts = presenter.getCurrentDisplayedData();

        assertEquals("ABC", adverts.get(0).getTitle());
        assertEquals("QYZ", adverts.get(1).getTitle());
        assertEquals("DEF", adverts.get(2).getTitle());
    }
}

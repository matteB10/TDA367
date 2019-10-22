package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.ClickDelayHelper;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ListPresenterTest {

    private ListPresenter presenter;
    private List<Advertisement> testData = new ArrayList<>();

    @Mock
    private ListView fragmentMock;
    @Mock
    private DataModel databaseMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private void createTestData() {
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("170101200000", "UniqueOwnerID", "UniqueAdID", "Title1","", 490, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("180101200000", "UniqueOwnerID", "UniqueAdID", "Title2","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("150101200000", "UniqueOwnerID", "UniqueAdID", "Title3","", 230, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("130101200000", "UniqueOwnerID", "UniqueAdID", "Title4","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "longTitleID", "Very Specific Name For Testing Purposes","", 639, Condition.GOOD,"", new ArrayList<>(),null));
    }

    private List emptyList() {
        return new ArrayList<>();
    }


    @Test
    public void testNumDataItemsIsCorrectWhenLoaded() {
        createTestData();
        Mockito.when(databaseMock.getAdsFromCurrentUser()).thenReturn(testData);
        presenter = new ProfilePresenter(fragmentMock, databaseMock);

        presenter.updateData();

        assertEquals(testData.size(), presenter.getItemCount());
    }

    @Test
    public void testIfDataIsNullListIsSizeZero() {
        Mockito.when(databaseMock.getAdsFromCurrentUser()).thenReturn(null);
        presenter = new ProfilePresenter(fragmentMock, databaseMock);

        presenter.updateData();

        assertEquals(0, presenter.getItemCount());
    }

    @Test
    public void testIfDataIsEmptyListIsSizeZero() {
        Mockito.when(databaseMock.getAdsFromCurrentUser()).thenReturn(emptyList());
        presenter = new ProfilePresenter(fragmentMock, databaseMock);

        presenter.updateData();

        assertEquals(0, presenter.getItemCount());
    }

    @Test
    public void testThatSortingIsNotPerformedWhenAsked() {
        createTestData();
        Mockito.when(databaseMock.getAllAdverts()).thenReturn(testData);
        HomePresenter presenter = new HomePresenter(fragmentMock, databaseMock);
        presenter.sortOptionSelected(3);

        presenter.updateData(testData, false);

        List<Advertisement> presenterData = presenter.getData();
        for (int i = 0; i < testData.size(); i++) {
            assert(testData.get(i).getTitle() == presenterData.get(i).getTitle());
        }
    }

    @Test
    public void testThatNoDataFoundLayoutIsSetWhenThereIsNoData() {
        Mockito.when(databaseMock.getUserFavourites()).thenReturn(emptyList());
        presenter = new FavoritesPresenter(fragmentMock, databaseMock);

        presenter.updateData();

        verify(fragmentMock, times(1)).showNoThumbnailsAvailableScreen();
    }

    @Test
    public void testSearchPerformedOnlyReturnOne() {
        createTestData();
        Mockito.when(databaseMock.getAllAdverts()).thenReturn(testData);
        HomePresenter presenter = new HomePresenter(fragmentMock, databaseMock);

        presenter.searchPerformed("Very Specific Name For Testing Purposes");

        assertEquals(1, presenter.getItemCount());
    }

    @Test
    public void testSearchPerformedWithNoQueryReturnsAllData() {
        createTestData();
        Mockito.when(databaseMock.getAllAdverts()).thenReturn(testData);
        HomePresenter presenter = new HomePresenter(fragmentMock, databaseMock);

        presenter.searchPerformed("");

        assertEquals(testData.size(), presenter.getItemCount());
    }

    @Test
    public void testThatDetailsViewIsNotShownWhenRowIsPressedWhenNoDataIsAvailable() {
        createTestData();
        Mockito.when(databaseMock.getUserFavourites()).thenReturn(emptyList());
        presenter = new FavoritesPresenter(fragmentMock, databaseMock);

        presenter.onRowPressed(testData.get(0).getUniqueID());

        verify(fragmentMock, times(0)).showDetailsScreen("");
    }

}

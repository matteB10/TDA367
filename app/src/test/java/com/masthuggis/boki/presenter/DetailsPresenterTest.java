package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.model.DataModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetailsPresenterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DetailsPresenter.View viewMock;
    @Mock
    private DataModel databaseMock;
    private Advertisement advert;
    private String id = "advertID";
    private DetailsPresenter presenter;

    @Before
    public void before() {
    }

    private void createAd() {
        advert = AdFactory.createAd("190101200000", "UniqueOwnerID", id, "Title","", 300, Condition.GOOD,"", new ArrayList<>(),null);
        when(databaseMock.getAdFromAdID(id)).thenReturn(advert);
    }

    private void setNoAd() {
        when(databaseMock.getAdFromAdID(id)).thenReturn(null);
    }

    private void initPresenter() {
        presenter = new DetailsPresenter(viewMock, id, databaseMock);
    }

    @Test
    public void setupOfViewIsDoneWhenCreated() {
        createAd();
        initPresenter();

        verify(viewMock, times(1)).setName(any());
        verify(viewMock, times(1)).setImageUrl(any());
        verify(viewMock, times(1)).setDate(any());
    }

    @Test
    public void itItHandlesScenarioIfNoAdvertIsFound() {
        setNoAd();
        initPresenter();

        verify(viewMock, times(1)).nothingToDisplay(any());
        verify(viewMock, times(0)).setName(any());
    }

    @Test
    public void ifContactOwnerIsPressedFirstTimeButtonTextShouldChange() {
        createAd();
        initPresenter();

        presenter.contactOwnerBtnClicked();

        verify(viewMock, times(1)).setOwnerButtonText(any());
    }

    @Test
    public void whenContactOwnerIsPressedOnceChatIsNotOpened() {
        Mockito.when(databaseMock.findChatID(any())).thenReturn(id);
        createAd();
        initPresenter();

        presenter.contactOwnerBtnClicked();

        verify(viewMock, times(0)).openChat(any());
    }

    @Test
    public void whenContactOwnerIsPressedTwiceChatIsOpened() {
        Mockito.when(databaseMock.findChatID(any())).thenReturn(id);
        createAd();
        initPresenter();

        presenter.contactOwnerBtnClicked();
        presenter.contactOwnerBtnClicked();

        verify(viewMock).openChat(advert.getUniqueID());
    }

    @Test
    public void ifContactOwnerIsPressedFTwiceButtonTextShouldChangeOnce() {
        createAd();
        initPresenter();

        presenter.contactOwnerBtnClicked();
        presenter.contactOwnerBtnClicked();

        verify(viewMock, times(1)).setOwnerButtonText(any());
    }

    @Test
    public void whenEditAdIsPressedViewIsNotified() {
        createAd();
        initPresenter();

        presenter.onChangedAdBtnPressed();

        verify(viewMock, times(1)).showEditView(any());
    }

    @Test
    public void whenAFavoriteIsAdded() {
        createAd();
        initPresenter();
        Mockito.when(databaseMock.isAFavourite(advert)).thenReturn(false);

        presenter.onFavouritesIconPressed();

        verify(viewMock, atLeastOnce()).setIsAFavouriteIcon();
        verify(databaseMock, times(1)).addToFavourites(advert);
    }

    @Test
    public void whenAFavoriteIsRemoved() {
        createAd();
        initPresenter();
        Mockito.when(databaseMock.isAFavourite(advert)).thenReturn(true);

        presenter.onFavouritesIconPressed();

        verify(viewMock, atLeastOnce()).setIsNotAFavouriteIcon();
        verify(databaseMock, times(1)).removeFromFavourites(advert);
    }

    @Test
    public void whenUserIsOwnerFavoriteButtonIsHidden() {
        Mockito.when(databaseMock.isUserOwner(any())).thenReturn(true);
        createAd();
        initPresenter();

        verify(viewMock, atLeastOnce()).hideFavouriteIcon();
    }

    @Test
    public void whenUserIsNotOwnerFavoriteButtonIsShown() {
        Mockito.when(databaseMock.isUserOwner(any())).thenReturn(false);
        createAd();
        initPresenter();

        verify(viewMock, times(0)).hideFavouriteIcon();
    }


}

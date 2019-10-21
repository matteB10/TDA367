package com.masthuggis.boki.presenter;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.Condition;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.view.CreateAdActivity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests basic funtionality of CreateAdPresenter
 * not depending on android or firebase
 */


public class CreateAdPresenterTest {

    // CreateAdPresenter presenter = new CreateAdPresenter(new MockView(), DependencyInjector.injectDataModel());
    @Mock
    private CreateAdPresenter presenter;

    @Mock
    private CreateAdActivity CreateAdActivityMock;
    @Mock
    private DataModel databaseMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private List<Advertisement> tenAds = new ArrayList<>();

    private List<Advertisement> generateAds() {
        for (int i = 0; i < 9; i++) {
            tenAds.add(AdFactory.createAd("datePublished",
                    "uniqueOwner", "id", "title " + i,
                    "", 100, Condition.GOOD, null, Collections.singletonList(""), "owner"));
        }
        return tenAds;
    }

    ;

    Advertisement advertisement = new Advert(
            "datePublished",
            "uniqueOwnerID",
            "id",
            "title",
            "description",
            123123,
            Condition.NEW,
            null,
            new ArrayList<>(),
            "owner");

    private void addAdToList(Advertisement ad) {
        tenAds.add(ad);
    }

    @Test
    public void testSaveAdvert() {
        CreateAdPresenter createAdPresenter = mock(CreateAdPresenter.class);
        doNothing().when(createAdPresenter).saveAdvert();


        //assertEquals(databaseMock.getAdFromAdID("id"),advertisement);
    }

    @Test
    public void testDeleteAdvert() {
        doNothing().when(databaseMock).saveAdvert(null, advertisement);
        doNothing().when(databaseMock).removeExistingAdvert(advertisement.getUniqueID(), advertisement.getUniqueOwnerID());
        assertFalse(databaseMock.getAdFromAdID("id").equals(advertisement));
    }

    @Test
    public void testUpdateAdvert() {
        advertisement.setTitle("updated title");
        advertisement.setPrice(100);
        advertisement.setDescription("updated description");
        //doNothing().when(databaseMock).saveAdvert(null, advertisement);
        doNothing().when(databaseMock).updateAd(null, advertisement);

        assertEquals(advertisement.getTitle(), "updated title");
        //verify(databaseMock, times(1)).);

    }

    @Test
    public void testTitleChanged() {
        String title = "new Title";
        Mockito.doNothing().when(presenter).titleChanged(title);
        verify(advertisement, times(1)).setTitle(title);
    }

    @Test
    public void testPriceChanged() {
        String price = "300";
        verify(advertisement, times(1)).setPrice(300);


    }

    @Test
    public void testDescriptionChanged() {
        presenter.descriptionChanged("new description");
        assertTrue(presenter.getAdvertisement().getDescription().equals("new description"));
    }

    @Test
    public void testPreDefTagsChanged() {
        assertEquals(0, presenter.getAdvertisement().getTags().size());

        String preDefTag = "Matematik";

        presenter.userDefTagsChanged(preDefTag);
        assertEquals(preDefTag, presenter.getAdvertisement().getTags().get(0));

        presenter.userDefTagsChanged(preDefTag);

        assertEquals(0, presenter.getAdvertisement().getTags().size());
    }

    @Test
    public void testUserDefTagsChanged() {
        assertEquals(0, presenter.getAdvertisement().getTags().size());

        String userTag = "new tag";

        presenter.userDefTagsChanged(userTag);
        assertEquals(userTag, presenter.getAdvertisement().getTags().get(0));
        presenter.userDefTagsChanged(userTag);

        assertEquals(0, presenter.getAdvertisement().getTags().size());
    }

    @Test
    public void testConditionChanged() {
        assertEquals(Condition.UNDEFINED, presenter.getAdvertisement().getCondition());
        presenter.conditionChanged(R.string.conditionGood);

        assertEquals(Condition.GOOD, presenter.getAdvertisement().getCondition());
        presenter.conditionChanged(R.string.conditionNew);

        assertEquals(Condition.NEW, presenter.getAdvertisement().getCondition());
    }


    @Test
    public void testEnablePublishButton() {
        MockView view = new MockView();
        CreateAdPresenter presenter = view.getPresenter();
        presenter.priceChanged("10");
        presenter.titleChanged("hej");
        presenter.conditionChanged(R.string.conditionGood);
        assertTrue(view.getPublishButtonEnabled());

        //if no title is given, button should be disabled
        presenter.titleChanged("");
        assertFalse(view.getPublishButtonEnabled());
    }


    //-----------------------------------------------------------------------
    class MockView implements CreateAdPresenter.View {
        CreateAdPresenter presenter;
        boolean publishButtonEnabled = false;

        MockView() {
            presenter = new CreateAdPresenter(this, DependencyInjector.injectDataModel());
        }


        private CreateAdPresenter getPresenter() {
            return presenter;
        }

        private boolean getPublishButtonEnabled() {
            return publishButtonEnabled;
        }

        @Override
        public void setTitle(String name) {
        }

        @Override
        public void setPrice(long price) {

        }

        @Override
        public void setImageUrl(String url) {

        }

        @Override
        public void setDescription(String description) {

        }

        @Override
        public void enablePublishButton(boolean isEnabled) {

        }

        @Override
        public void enableSaveButton(boolean b) {

        }

        @Override
        public void styleConditionButtonPressed(int condition) {

        }

        @Override
        public void setPreDefTagSelected(String tag, boolean isPressed) {

        }

        @Override
        public void displayUserTagButton(String tag) {

        }

        @Override
        public void removeUserTagButton(String tag) {

        }

        @Override
        public File getCurrentImageFile() {
            return null;
        }

        @Override
        public void setTags(List<String> tags) {
        }

        @Override
        public void toggleCondition(String id) {

        }

        @Override
        public void displayNotFoundToast(String toast) {

        }
    }
}


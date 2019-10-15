package com.masthuggis.boki.presenter;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests basic funtionality of CreateAdPresenter
 * not depending on android or firebase
 */


public class CreateAdPresenterTest {

    CreateAdPresenter presenter = new CreateAdPresenter(new MockView(),
            DependencyInjector.injectDataModel());
    @Mock
    private DataModel databaseMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    Advertisement advertisement = new Advert(
            "datePublished",
            "uniqueOwnerID",
            "id",
            "title",
            "description",
            123123,
            Advert.Condition.NEW,
            null,
            new ArrayList<>(),
            "owner");
    //--------------------------------------------------------------------------

    @Test
    public void testSaveAdvert() {
        Mockito.doNothing().when(databaseMock).saveAdvert(null, advertisement);
        assertEquals(databaseMock.getAdFromAdID("id"),advertisement);
    }

    @Test
    public void testDeleteAdvert() {
        Mockito.doNothing().when(databaseMock).saveAdvert(null, advertisement);
        Mockito.doNothing().when(databaseMock).removeExistingAdvert(advertisement);
        assertFalse(databaseMock.getAdFromAdID("id").equals(advertisement));

    }

    @Test
    public void testUpdateAdvert() {
        advertisement.setTitle("updated title");
        advertisement.setPrice(100);
        advertisement.setDescription("updated description");
        Mockito.doNothing().when(databaseMock).saveAdvert(null, advertisement);
        Mockito.doNothing().when(databaseMock).updateAd(null, advertisement);

        assertEquals(advertisement.getTitle(), databaseMock.getAdFromAdID("id").getTitle());

    }

    @Test
    public void testTitleChanged() {
        presenter.titleChanged("new title");
        assertTrue(presenter.getAdvertisement().getTitle() == "new title");

    }

    @Test
    public void testPriceChanged() {
        presenter.priceChanged("800000");
        assertEquals(0, presenter.getAdvertisement().getPrice());

        presenter.priceChanged("300");
        assertEquals(300, presenter.getAdvertisement().getPrice());
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
        assertEquals(Advert.Condition.UNDEFINED, presenter.getAdvertisement().getCondition());
        presenter.conditionChanged(R.string.conditionGood);

        assertEquals(Advert.Condition.GOOD, presenter.getAdvertisement().getCondition());
        presenter.conditionChanged(R.string.conditionNew);

        assertEquals(Advert.Condition.NEW, presenter.getAdvertisement().getCondition());
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

        private CreateAdPresenter getPresenter() { return presenter; }

        private boolean getPublishButtonEnabled() { return publishButtonEnabled; }

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
        public File getCurrentImageFile() { return null; }

        @Override
        public void setTags(List<String> tags) { }
    }
}

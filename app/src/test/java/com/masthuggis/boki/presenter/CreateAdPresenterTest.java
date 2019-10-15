package com.masthuggis.boki.presenter;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.model.Advert;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests basic funtionality of CreateAdPresenter
 * not depending on android or firebase
 */


public class CreateAdPresenterTest {

    class MockView implements CreateAdPresenter.View {
        CreateAdPresenter presenter;
        boolean buttonEnabled = false;

        MockView(){
            presenter = new CreateAdPresenter(this, DependencyInjector.injectDataModel());
        }

        private CreateAdPresenter getPresenter(){
            return presenter;
        }
        private boolean getButtonEnabled(){
            return buttonEnabled;
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
            buttonEnabled = isEnabled;
        }
        @Override
        public void styleConditionButtonPressed(int condition) {
        }
        @Override
        public void setTagStyling(String tag, boolean isPressed) {
        }
        @Override
        public void displayUserTagButton(String tag){
            
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
    }


    @Test
    public void testChangedTitle() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView(), DependencyInjector.injectDataModel());

        presenter.titleChanged("new title");
        assertTrue(presenter.getAdvertisement().getTitle() == "new title");
    }


    @Test
    public void testChangedDescription() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView(), DependencyInjector.injectDataModel());
        presenter.descriptionChanged("new description");
        assertTrue(presenter.getAdvertisement().getDescription().equals("new description"));
    }

    @Test
    public void testChangedPrice() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView(), DependencyInjector.injectDataModel());
        presenter.priceChanged("800000");
        assertEquals(0,presenter.getAdvertisement().getPrice());

        presenter.priceChanged("300");
        assertEquals(300, presenter.getAdvertisement().getPrice());
    }
    @Test
    public void testChangedTags(){
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView(), DependencyInjector.injectDataModel());
        assertEquals(0,presenter.getAdvertisement().getTags().size());

        String subjectTag = "Matematik";

        presenter.preDefTagsChanged(subjectTag);
        assertEquals(subjectTag,presenter.getAdvertisement().getTags().get(0));
        presenter.preDefTagsChanged(subjectTag);

        assertEquals(0,presenter.getAdvertisement().getTags().size());
    }
    @Test
    public void testConditionChanged(){
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView(), DependencyInjector.injectDataModel());
        assertEquals(Advert.Condition.UNDEFINED, presenter.getAdvertisement().getCondition());
        presenter.conditionChanged(R.string.conditionGood);
        assertEquals(Advert.Condition.GOOD, presenter.getAdvertisement().getCondition());
        presenter.conditionChanged(R.string.conditionNew);
        assertEquals(Advert.Condition.NEW, presenter.getAdvertisement().getCondition());
    }
    @Test
    public void testEnablePublishButton(){
        MockView view = new MockView();
        CreateAdPresenter presenter = view.getPresenter();
        presenter.priceChanged("10");
        presenter.titleChanged("hej");
        presenter.conditionChanged(R.string.conditionGood);
        assertTrue(view.getButtonEnabled());

        //if no title is given, button should be disabled
        presenter.titleChanged("");
        assertFalse(view.getButtonEnabled());
    }


}

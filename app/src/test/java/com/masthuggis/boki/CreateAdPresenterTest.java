package com.masthuggis.boki;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.presenter.CreateAdPresenter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests basic funtionality of CreateAdPresenter
 * not depending on android or firebase
 */


public class CreateAdPresenterTest {

    class MockView implements CreateAdPresenter.View {
        CreateAdPresenter presenter;
        boolean buttonEnabled = false;

        MockView(){
            presenter = new CreateAdPresenter(this);
        }

        private CreateAdPresenter getPresenter(){
            return presenter;
        }
        private boolean getButtonEnabled(){
            return buttonEnabled;
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
    }


    @Test
    public void testChangedTitle() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());

        presenter.titleChanged("new title");
        assertTrue(presenter.getAdvertisement().getTitle() == "new title");
    }


    @Test
    public void testChangedDescription() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        presenter.descriptionChanged("new description");
        assertTrue(presenter.getAdvertisement().getDescription().equals("new description"));
    }

    @Test
    public void testChangedPrice() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        presenter.priceChanged("800000");
        assertEquals(0,presenter.getAdvertisement().getPrice());

        presenter.priceChanged("300");
        assertEquals(300, presenter.getAdvertisement().getPrice());
    }
    @Test
    public void testChangedTags(){
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        assertEquals(0,presenter.getAdvertisement().getTags().size());

        String subjectTag = "Matematik";

        presenter.preDefTagsChanged(subjectTag);
        assertEquals(subjectTag,presenter.getAdvertisement().getTags().get(0));
        presenter.preDefTagsChanged(subjectTag);

        assertEquals(0,presenter.getAdvertisement().getTags().size());
    }
    @Test
    public void testConditionChanged(){
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
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

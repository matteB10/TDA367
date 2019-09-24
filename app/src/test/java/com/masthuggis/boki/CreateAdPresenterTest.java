package com.masthuggis.boki;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.presenter.CreateAdPresenter;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests basic funtionality of CreateAdPresenter
 * not depending on android or firebase
 */


public class CreateAdPresenterTest {

    class MockView implements CreateAdPresenter.View {
        public void enablePublishButton() {

        }
    }


    @Test
    public void testChangedTitle() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());

        presenter.titleChanged("new title");
        assertTrue(presenter.getAdvertisement().getTitle() == "new title");
    }

    @Test
    public void testChangedImage() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        assertEquals("",presenter.getAdvertisement().getImgURL());

        presenter.imageURIChanged("http://java.sun.com/j2se/1.3/");
        assertTrue(presenter.getAdvertisement().getImgURL().equals("http://java.sun.com/j2se/1.3/"));
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

        presenter.tagsChanged(subjectTag);
        assertEquals(subjectTag,presenter.getAdvertisement().getTags().get(0));
        presenter.tagsChanged(subjectTag);

        assertEquals(0,presenter.getAdvertisement().getTags().size());
    }
    @Test
    public void testConditionChanged(){
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        assertEquals(Advert.Condition.UNDEFINED, presenter.getAdvertisement().getCondition());
        presenter.conditionChanged(Advert.Condition.GOOD);
        assertEquals(Advert.Condition.GOOD, presenter.getAdvertisement().getCondition());
        presenter.conditionChanged(Advert.Condition.NEW);
        assertEquals(Advert.Condition.NEW, presenter.getAdvertisement().getCondition());
    }

}

package com.masthuggis.boki;


import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.presenter.CreateAdPresenter;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class CreateAdPresenterTest {

    class MockView implements CreateAdPresenter.View {
        public void enablePublishButton() {

        }
    }


    @BeforeClass
    public static void runOnceBeforeClass() {
        System.out.println("@BeforeClass - runOnceBeforeClass");
    }

    @AfterClass
    public static void runOnceAfterClass() {
        System.out.println("@AfterClass - runOnceAfterClass");
    }


    @Test
    public void testChangedTitle() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());

        presenter.titleChanged("new title");
        assertTrue(presenter.getTitle() == "new title");
    }

    @Test
    public void testChangedImageURI() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        assertNull(presenter.getImageUri());

        presenter.imageURIChanged("http://java.sun.com/j2se/1.3/");
        assertNotNull(presenter.getImageUri());
        assertTrue(presenter.getImageUri().equals("http://java.sun.com/j2se/1.3/"));

        presenter.imageURIChanged("hej");
        assertFalse(presenter.getImageUri() == "hej");
    }

    @Test
    public void testChangedDescription() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        presenter.descriptionChanged("new description");
        assertTrue(presenter.getDescription().equals("new description"));
    }

    @Test
    public void testChangedPrice() {
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        presenter.priceChanged("800000");

        assertEquals(0,presenter.getPrice());
        presenter.priceChanged("300");

        assertEquals(300, presenter.getPrice());
    }
    @Test
    public void testChangedTags(){
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        assertNull(presenter.getTags());

        String subjectTag = "Matematik";

        presenter.tagsChanged(subjectTag);
        assertEquals(subjectTag,presenter.getTags().get(0));
        presenter.tagsChanged(subjectTag);

        assertEquals(0,presenter.getTags().size());
    }
    @Test
    public void testConditionChanged(){
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        assertNull(presenter.getCondition());
        presenter.conditionChanged(Advert.Condition.GOOD);
        assertEquals(Advert.Condition.GOOD, presenter.getCondition());
        presenter.conditionChanged(Advert.Condition.NEW);
        assertEquals(Advert.Condition.NEW, presenter.getCondition());
    }
    @Test
    public void testCreateAdvert(){
        CreateAdPresenter presenter = new CreateAdPresenter(new MockView());
        presenter.conditionChanged(Advert.Condition.GOOD);
        presenter.tagsChanged("hej");
        presenter.priceChanged("100");
        presenter.imageURIChanged("http://java.sun.com/j2se/1.3/");
        presenter.descriptionChanged("info");
        presenter.titleChanged("calculus");
        int numberOfAds = Repository.getInstance().getTemporaryListOfAllAds().size();
        presenter.publishAdvert();
        assertEquals((numberOfAds + 1), Repository.getInstance().getTemporaryListOfAllAds().size());

    }

}

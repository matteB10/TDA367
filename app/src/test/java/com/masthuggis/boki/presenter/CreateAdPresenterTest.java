package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.Condition;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.view.CreateAdActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static com.masthuggis.boki.model.Condition.GOOD;
import static com.masthuggis.boki.model.Condition.NEW;
import static com.masthuggis.boki.model.Condition.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

/**
 * Tests basic funtionality of CreateAdPresenter
 * not depending on android or firebase
 */


public class CreateAdPresenterTest {
    CreateAdPresenter presenter;
    ArgumentCaptor<Boolean> booleanCapture;


    @Mock
    private DataModel databaseMock;
    @Mock
    private CreateAdActivity mockView;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    Advertisement advertisement = new Advert(
            "datePublished",
            "uniqueOwnerID",
            "id",
            "title",
            "description",
            123123,
            NEW,
            "imgUrl",
            new ArrayList<>(),
            "owner");


    @Before
    public void before() {
        presenter = new CreateAdPresenter(mockView, databaseMock);
        booleanCapture = ArgumentCaptor.forClass(Boolean.class);
    }

    //--------------------------------------------------------------------------

    @Test
    public void testSaveAdvert() {
        doNothing().when(databaseMock).saveAdvert(null, advertisement);
        assertEquals(databaseMock.getAdFromAdID("id"),advertisement);
    }

    @Test
    public void testDeleteAdvert() {
        doNothing().when(databaseMock).saveAdvert(null, advertisement);
        doNothing().when(databaseMock).removeExistingAdvert(advertisement.getUniqueID(),advertisement.getUniqueOwnerID());
        assertFalse(databaseMock.getAdFromAdID("id").equals(advertisement));
    }

    @Test
    public void testUpdateAdvert() {
        advertisement.setTitle("updated title");
        advertisement.setPrice(100);
        advertisement.setDescription("updated description");
        doNothing().when(databaseMock).saveAdvert(null, advertisement);
        doNothing().when(databaseMock).updateAd(null, advertisement);

        assertEquals(advertisement.getTitle(), databaseMock.getAdFromAdID("id").getTitle());
    }

    @Test
    public void testTitleChanged() {
        presenter.titleChanged("new title");
        assertTrue(presenter.getAdvertisement().getTitle().equals("new title"));

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
        ArgumentCaptor<Condition> conditionArgumentCaptor = ArgumentCaptor.forClass(Condition.class);

        doNothing().when(mockView).setCondition(conditionArgumentCaptor.capture(),booleanCapture.capture());

        assertEquals(UNDEFINED, presenter.getCondition()); //on startup, condition not set

        presenter.conditionChanged(GOOD); //when presenter/model is updated, view is updated as well
        assertEquals(GOOD,conditionArgumentCaptor.getValue());
        assertEquals(true,booleanCapture.getValue());
        assertEquals(GOOD, presenter.getCondition()); //check that presenter also returns correct value

        presenter.conditionChanged(GOOD); //if same condition button clicked again, changes status from selected in view
        assertEquals(GOOD,conditionArgumentCaptor.getValue());
        assertEquals(false, booleanCapture.getValue());
        assertEquals(UNDEFINED,presenter.getCondition()); //check that presenter also returns correct value

        presenter.conditionChanged(NEW);
        assertEquals(NEW, presenter.getCondition());
    }


    @Test
    public void testEnablePublishButton() {

        doNothing().when(mockView).enablePublishButton(booleanCapture.capture());
        presenter.priceChanged("10");
        presenter.titleChanged("hej");
        presenter.conditionChanged(GOOD);
        presenter.imageChanged();
        //if all fields valid in presenter, enablePublishButton method is called in view
        assertEquals(true, booleanCapture.getValue());

        presenter.titleChanged(""); //empty title is not valid value, should disable publishbutton
        assertEquals(false,booleanCapture.getValue());

    }
    @Test
    public void testEnableSaveButton() {

        doNothing().when(mockView).enableSaveButton(booleanCapture.capture());
        presenter.priceChanged("10");
        presenter.titleChanged("hej");
        presenter.conditionChanged(NEW);
        presenter.imageChanged();
        //if all fields valid in presenter, enableSaveButton method is called in view
        assertEquals(true, booleanCapture.getValue());

        presenter.titleChanged(""); //empty title is not valid value, should disable savebutton
        assertEquals(false,booleanCapture.getValue());
    }




}

package com.masthuggis.boki.presenter;

import com.masthuggis.boki.MockRepository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.view.CreateAdActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static com.masthuggis.boki.utils.Condition.GOOD;
import static com.masthuggis.boki.utils.Condition.NEW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

/**
 * Tests basic funtionality of CreateAdPresenter
 * not depending on android or firebase
 */


public class CreateAdPresenterTest {
    CreateAdPresenter presenter;
    ArgumentCaptor<Boolean> booleanCapture;
    List<Advertisement> ads;


    @Mock
    private DataModel databaseMock;
    @Mock
    private CreateAdActivity mockView;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Before
    public void before() {
        presenter = new CreateAdPresenter(mockView, databaseMock);
        booleanCapture = ArgumentCaptor.forClass(Boolean.class);
        ads = MockRepository.getTestAds();
    }

    //--------------------------------------------------------------------------


    @Test
    public void testSaveAdvert() {
        CreateAdPresenter createAdPresenter = mock(CreateAdPresenter.class);
        doNothing().when(createAdPresenter).saveAdvert();

    }

    @Test
    public void testUpdateAdvert() {
        Advertisement advertisement = ads.get(0); //get one advert
        advertisement.setTitle("updated title");
        advertisement.setPrice(100);
        advertisement.setDescription("updated description");
        //doNothing().when(databaseMock).saveAdvert(null, advertisement);
        doNothing().when(databaseMock).updateAd(null, advertisement);

        assertEquals(advertisement.getTitle(), "updated title");
        //verify(databaseMock, times(1)).);

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


        presenter.conditionChanged(GOOD); //when presenter/model is updated, view is updated as well
        assertEquals(GOOD,conditionArgumentCaptor.getValue());
        assertEquals(true,booleanCapture.getValue());


        presenter.conditionChanged(GOOD); //if same condition button clicked again, changes status from selected in view
        assertEquals(GOOD,conditionArgumentCaptor.getValue());
        assertEquals(false, booleanCapture.getValue());

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

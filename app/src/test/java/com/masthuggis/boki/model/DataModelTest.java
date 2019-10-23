package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.BackendFactory;
import com.masthuggis.boki.backend.iBackend;
import com.masthuggis.boki.model.callbacks.FavouriteIDsCallback;
import com.masthuggis.boki.model.callbacks.advertisementCallback;
import com.masthuggis.boki.model.callbacks.chatCallback;
import com.masthuggis.boki.model.callbacks.userCallback;
import com.masthuggis.boki.presenter.ChatMessagesHelper;
import com.masthuggis.boki.utils.Condition;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RepositoryFactory.class, BackendFactory.class})
public class DataModelTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private iRepository repositoryMock;
    @Mock
    private iUser userMock;
    @Mock
    private iBackend backendMock;
    private List<Advertisement> testData;
    private List<Advertisement> favorites;
    private List<iChat> chats = ChatMessagesHelper.generateUserChats();
    private DataModel dataModel;
    private String userID = "userID";

    @Before
    public void before() {
        if (testData == null) {
            testData = new ArrayList<>();
            testData.add(AdFactory.createAd( "22/10/19:13:16:00", userID, "UniqueAdID", "ABC","", 300, Condition.GOOD,"", new ArrayList<>(),null));
            testData.add(AdFactory.createAd( "22/10/19:10:16:00", "UniqueOwnerID", "UniqueAdID1", "QYZ","", 490, Condition.GOOD,"", new ArrayList<>(),null));
            testData.add(AdFactory.createAd( "22/10/19:10:15:00", "UniqueOwnerID", "UniqueAdID2", "DEF","", 299, Condition.GOOD,"", new ArrayList<>(),null));

            favorites = createFavoriteAdsFromTestData();

            initDataModel();
        }
    }

    private void initDataModel() {
        PowerMockito.mockStatic(RepositoryFactory.class);
        PowerMockito.mockStatic(BackendFactory.class);
        Mockito.when(BackendFactory.createBackend()).thenReturn(backendMock);
        Mockito.when(RepositoryFactory.createRepository(any())).thenReturn(repositoryMock);

        when(userMock.getId()).thenReturn(userID);
        when(userMock.getFavourites()).thenReturn(favorites);
        when(userMock.getChats()).thenReturn(chats);

        setDataModelUserTo(userMock);
        setDataModelAdvertsTo(testData);
        setDataModelFavoriteIDsTo(getFavoriteIDsFromTestdata());
        setDataModelUserChatsTo(chats);

        dataModel = DataModel.getInstance();
        dataModel.initUser(() -> {});
    }

    private void setDataModelUserTo(iUser user) {
        Mockito.doAnswer(invocation -> {
            userCallback callback = (userCallback) invocation.getArguments()[0];
            callback.onCallback(user);
            return null;
        }).when(repositoryMock).getUser(any());
    }

    private void setDataModelAdvertsTo(List<Advertisement> adverts) {
        Mockito.doAnswer(invocation -> {
            advertisementCallback callback = (advertisementCallback) invocation.getArguments()[0];
            callback.onCallback(adverts);
            return null;
        }).when(repositoryMock).initialAdvertFetch(any());
    }

    private void setDataModelFavoriteIDsTo(List<String> ids) {
        Mockito.doAnswer(invocation -> {
            FavouriteIDsCallback callback = (FavouriteIDsCallback) invocation.getArguments()[1];
            callback.onCallback(ids);
            return null;
        }).when(repositoryMock).getUserFavourites(any(), any());
    }

    private void setDataModelUserChatsTo(List<iChat> chats) {
        Mockito.doAnswer(invocation -> {
            chatCallback callback = (chatCallback) invocation.getArguments()[1];
            callback.onCallback(chats);
            return null;
        }).when(repositoryMock).getUserChats(any(), any());
    }

    private List<String> getFavoriteIDsFromTestdata() {
        List<String> favoriteIDs = new ArrayList<>();
        for (Advertisement ad: testData) {
            favoriteIDs.add(ad.getUniqueID());
        }
        return favoriteIDs;
    }

    private List<Advertisement> createFavoriteAdsFromTestData() {
        List<Advertisement> favoriteAds = new ArrayList<>();
        favoriteAds.add(testData.get(0));
        return favoriteAds;
    }

    /**
     * All initialization tests must be done in the same Test function because DataModel is a Singleton
     * which makes separating it into multiple methods difficult.
     */
    @Test
    public void testUserInitializationIsCorrect() {
        // Verifies that repository is created and that it is only done once.
        PowerMockito.verifyStatic(Mockito.times(1));
        RepositoryFactory.createRepository(backendMock);

        // Verifies that user is set and uses the correct ID.
        assertEquals(userID, dataModel.getUserID());

        // Verifies that user favorites are set and uses correct data.
        ArgumentCaptor<List<Advertisement>> favoritesArgument = ArgumentCaptor.forClass(List.class);
        verify(userMock).setFavourites(favoritesArgument.capture());
        assertEquals(favorites.get(0).getTitle(), favoritesArgument.getValue().get(0).getTitle());

        // Verifies that user chats are set and uses correct data.
        ArgumentCaptor<List<iChat>> chatsArguments = ArgumentCaptor.forClass(List.class);
        verify(userMock).setChats(chatsArguments.capture());
        for (int i = 0; i < chats.size(); i++) {
            iChat chat = chats.get(i);
            assertEquals(chat.getChatID(), chatsArguments.getValue().get(i).getChatID());
        }
    }

    @Test
    public void getAdByIDReturnsCorrectAd() {
        Advertisement testedAd = testData.get(1);

        Advertisement dataModelAd = dataModel.getAdFromAdID(testedAd.getUniqueID());

        assertEquals(testedAd.getUniqueID(), dataModelAd.getUniqueID());
    }

    @Test
    public void returnsValidFavorites() {
        Advertisement testedAd = favorites.get(0);

        Advertisement dataModelAd = dataModel.getUserFavourites().get(0);

        assertEquals(testedAd.getUniqueID(), dataModelAd.getUniqueID());
    }

    @Test
    public void isAFavoriteReturnTrueWhenFavoriteExist() {
        Advertisement testedAd = favorites.get(0);

        boolean isAFavorite = dataModel.isAFavourite(testedAd);

        assertEquals(true, isAFavorite);
    }

    @Test
    public void isAFavoriteReturnFalseWhenFavoriteDoesNotExist() {
        Advertisement testedAd = testData.get(1);

        boolean isAFavorite = dataModel.isAFavourite(testedAd);

        assertEquals(false, isAFavorite);
    }

    @Test
    public void findChatByIDReturnNullWhenNoChatItExist() {
        iChat testedChat = ChatMessagesHelper.generateUserChats().get(0);

        iChat dataModelChat = dataModel.findChatByID(testedChat.getChatID());

        assertEquals(null, dataModelChat);
    }
}

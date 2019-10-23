package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.BackendFactory;
import com.masthuggis.boki.backend.iBackend;
import com.masthuggis.boki.model.callbacks.advertisementCallback;
import com.masthuggis.boki.model.callbacks.userCallback;
import com.masthuggis.boki.utils.Condition;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

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
    private List<Advertisement> testData = new ArrayList<>();
    private DataModel dataModel;
    private String userID = "userID";

    @Before
    public void before() {
        testData.add(AdFactory.createAd( "22/10/19:13:16:00", "UniqueOwnerID", "UniqueAdID", "ABC","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd( "22/10/19:10:16:00", "UniqueOwnerID", "UniqueAdID", "QYZ","", 490, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd( "22/10/19:10:15:00", "UniqueOwnerID", "UniqueAdID", "DEF","", 299, Condition.GOOD,"", new ArrayList<>(),null));

        initDataModel();
    }

    private void initDataModel() {
        PowerMockito.mockStatic(RepositoryFactory.class);
        PowerMockito.mockStatic(BackendFactory.class);
        Mockito.when(BackendFactory.createBackend()).thenReturn(backendMock);
        Mockito.when(RepositoryFactory.createRepository(any())).thenReturn(repositoryMock);

        when(userMock.getId()).thenReturn(userID);

        setDataModelUserTo(userMock);
        setDataModelAdvertsTo(testData);
        dataModel = DataModel.getInstance();
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

    @Test
    public void repositoryIsCreatedWhenDataModelIsInitialized() {
        PowerMockito.verifyStatic(Mockito.times(1));
        RepositoryFactory.createRepository(backendMock);
    }

    @Test
    public void userIsInitializedIfNoUserExists() {
        dataModel.initUser(() -> {});

        assertEquals(userID, dataModel.getUserID());
    }



    /*
        Mockito.doAnswer(invocation -> {
            SuccessCallback callback = (SuccessCallback) invocation.getArguments()[0];
            callback.onSuccess();
            return null;
        }).when(databaseMock).initUser(any(SuccessCallback.class));

     */

}

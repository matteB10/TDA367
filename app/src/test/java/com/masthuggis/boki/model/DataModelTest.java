package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.BackendFactory;
import com.masthuggis.boki.backend.iBackend;
import com.masthuggis.boki.presenter.ChatPresenter;
import com.masthuggis.boki.utils.Condition;

import org.junit.Before;
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

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.lang.reflect.Field;
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
    private List<Advertisement> adverts = new ArrayList<>();
    private DataModel dataModel;

    @Before
    public void before() {
        adverts.add(AdFactory.createAd( "22/10/19:13:16:00", "UniqueOwnerID", "UniqueAdID", "ABC","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        adverts.add(AdFactory.createAd( "22/10/19:10:16:00", "UniqueOwnerID", "UniqueAdID", "QYZ","", 490, Condition.GOOD,"", new ArrayList<>(),null));
        adverts.add(AdFactory.createAd( "22/10/19:10:15:00", "UniqueOwnerID", "UniqueAdID", "DEF","", 299, Condition.GOOD,"", new ArrayList<>(),null));
    }

    @Test
    public void repositoryIsCreatedWhenDataModelIsInitialized() {
        PowerMockito.mockStatic(RepositoryFactory.class);
        PowerMockito.mockStatic(BackendFactory.class);
        Mockito.when(BackendFactory.createBackend()).thenReturn(backendMock);
        Mockito.when(RepositoryFactory.createRepository(backendMock)).thenReturn(repositoryMock);

        dataModel = DataModel.getInstance();

        PowerMockito.verifyStatic(Mockito.times(1));
        RepositoryFactory.createRepository(backendMock);
    }

}

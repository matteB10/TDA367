package com.masthuggis.boki.model;

import com.masthuggis.boki.presenter.ChatPresenter;
import com.masthuggis.boki.utils.Condition;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DataModelTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private RepositoryFactory repositoryFactoryMock;
    @Mock
    private iRepository repositoryMock;
    @Mock
    private iUser userMock;
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
        /*
        dataModel = DataModel.getInstance();

        verify(repositoryFactoryMock).
         */
    }

}

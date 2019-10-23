package com.masthuggis.boki.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.masthuggis.boki.MockRepository;
import com.masthuggis.boki.backend.BackendDataHandler;
import com.masthuggis.boki.backend.BackendFactory;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.RepositoryFactory;
import com.masthuggis.boki.backend.iRepository;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.presenter.ChatPresenter;
import com.masthuggis.boki.utils.Condition;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

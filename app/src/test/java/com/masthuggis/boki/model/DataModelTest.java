package com.masthuggis.boki.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.masthuggis.boki.backend.BackendDataHandler;
import com.masthuggis.boki.backend.BackendFactory;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.presenter.ChatPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DataModelTest {


    @Mock
    BackendDataHandler mockBackend;


    @Mock
    ChatPresenter mockChatPresenter;

    @Mock
    Repository mockRepo;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void resetSingleton()throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = DataModel.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    /*@Test
    public void testAddChatObserver() {

        when(BackendFactory.createBackend()).thenReturn(mockBackend);
        when(BackendFactory.createBackendReader(new ArrayList<>())).thenReturn(null);
        when(BackendFactory.createBackendReader(new ArrayList<>())).thenReturn(null);
        DataModel model = DataModel.getInstance();
        assert (model.getChatObservers().size() == 0);
        model.addChatObserver(mockChatPresenter);
        assert (model.getChatObservers().size() == 1);
    }*/
}

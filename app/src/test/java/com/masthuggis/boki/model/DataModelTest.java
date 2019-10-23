package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.BackendDataHandler;
import com.masthuggis.boki.presenter.ChatPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;

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

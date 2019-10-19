package com.masthuggis.boki.presenter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.Condition;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.view.ChatFragment;
import com.masthuggis.boki.view.ListView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListPresenterTest {

    private ListPresenter presenter;
    private ListPresenterView view;
    private boolean onCreateHeaderCalled;
    private boolean onCreateNoResultsFoundCalled;
    private List<Advertisement> testData = new ArrayList<>();

    @Mock
    private ListView fragmentMock;
    @Mock
    private DataModel databaseMock;
    @Mock
    private ChatFragment chatFragmentMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void before() {
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Condition.GOOD,"", new ArrayList<>(),null));

        Mockito.when(databaseMock.getAdsFromCurrentUser()).thenReturn(testData);
    }

    private void init() {
        /*
        //        Mockito.doNothing().when(chatFragmentMock).showUserChats(null);
        Mockito.doNothing().when(chatFragmentMock).hideLoadingScreen();
        Mockito.when(databaseMock.getUserChats()).thenReturn(userChats);
        //      Mockito.doNothing().when(databaseMock).addChatObserver(null);
        addMessagesToChats(userChats);
        when(databaseMock.getUserID()).thenReturn("");
         */
    }

    @Test
    public void testNumDataItemsIsCorrectWhenLoaded() {
        presenter = new ProfilePresenter(fragmentMock, databaseMock);

        presenter.updateData();

        assertEquals(testData.size(), presenter.getItemCount());
    }

    class MockPresenter extends ListPresenter {
        MockPresenter(ListPresenterView view, DataModel dataModel) {
            super(view, dataModel);
        }

        @Override
        public List<Advertisement> getData() {
            return testData;
        }

        @Override
        public List sort(List data) {
            return data;
        }

        @Override
        public void onBindThumbnailViewAtPosition(int position, Object dataView) {

        }
    }

    class MockView extends ListView {
        @Override
        protected View onCreateHeaderLayout() {
            onCreateHeaderCalled = true;
            return null;
        }

        @Override
        protected View onCreateNoResultsFoundLayout() {
            onCreateNoResultsFoundCalled = true;
            return null;
        }

        @Override
        protected RecyclerView.Adapter getAdapter() {
            return null;
        }
    }

}

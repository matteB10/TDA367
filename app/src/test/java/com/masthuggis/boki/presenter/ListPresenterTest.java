package com.masthuggis.boki.presenter;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.sorting.SortManager;
import com.masthuggis.boki.view.ListView;
import com.masthuggis.boki.view.PullToRefreshCallback;

import org.junit.Test;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListPresenterTest {

    private ListPresenter presenter;
    private ListPresenterView view;
    private boolean onCreateHeaderCalled;
    private boolean onCreateNoResultsFoundCalled;
    @Mock private DataModel databaseMock;
    private List<Advertisement> testData;

    @Before
    public void before() {
        onCreateHeaderCalled = false;
        onCreateNoResultsFoundCalled = false;
        presenter = new MockPresenter(new MockView(), DependencyInjector.injectDataModel());

        Mockito.doNothing().when(databaseMock);

        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Advert.Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Advert.Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Advert.Condition.GOOD,"", new ArrayList<>(),null));
    }

    @Test
    public void testNumAdvertsIsCorrect() {
        assertEquals(presenter.getItemCount(), testData.size());
    }


    class MockPresenter extends ListPresenter {
        public MockPresenter(ListPresenterView view, DataModel dataModel) {
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

        @Override
        protected RecyclerView.LayoutManager getLayoutManager() {
            return null;
        }

        @Override
        protected RecyclerView.ItemDecoration getSpacingDecorator() {
            return null;
        }

        @Nullable
        @Override
        protected PullToRefreshCallback optionalPullToRefreshHandler() {
            return null;
        }
    }

}

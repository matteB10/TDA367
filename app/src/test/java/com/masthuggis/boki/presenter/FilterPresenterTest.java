package com.masthuggis.boki.presenter;

import com.masthuggis.boki.utils.Filter;
import com.masthuggis.boki.view.FilterFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

public class FilterPresenterTest {
    FilterPresenter presenter;

    @Mock
    private FilterFragment filterFragmentMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void before(){
        presenter = new FilterPresenter(filterFragmentMock);
    }



    @Test
    public void changePriceTest(){
        presenter.maxPriceChanged(100);
        assertEquals(100,Filter.getInstance().getMaxPrice());
    }
    @Test
    public void changeFiltersTest(){
        //Test that new filter is added
        presenter.tagsChanged("Kemi");
        assertEquals("Kemi",Filter.getInstance().getTags().get(0));
        assertEquals(1,Filter.getInstance().getTags().size());
        //Test if filter is removed if clicked again
        presenter.tagsChanged("Kemi");
        assertEquals(0,Filter.getInstance().getTags().size());
    }
    @Test
    public void testSetUp(){
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> booleanCaptor = ArgumentCaptor.forClass(Boolean.class);

        doNothing().when(filterFragmentMock).setMaxPrice(integerArgumentCaptor.capture());
        doNothing().when(filterFragmentMock).setUpFilters(stringCaptor.capture(),booleanCaptor.capture());

        Filter.getInstance().setMaxPrice(50);
        Filter.getInstance().setTags(new ArrayList<>());
        presenter.setUpView();

        assertEquals(50,(long) integerArgumentCaptor.getValue());
    }



}

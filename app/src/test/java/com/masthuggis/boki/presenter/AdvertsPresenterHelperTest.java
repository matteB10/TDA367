package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.view.ThumbnailView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class AdvertsPresenterHelperTest {
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock private ThumbnailView viewMock;
    private List<Advertisement> testData = new ArrayList<>();
    private Advertisement testAdvert;

    @Before
    public void before() {
        testData.add(AdFactory.createAd("22/10/19:13:16:00", "UniqueOwnerID", "UniqueAdID", "ABC", "", 300, Condition.GOOD, "imgUrl", new ArrayList<>(), null));
        testData.add(AdFactory.createAd("22/10/19:10:16:00", "UniqueOwnerID", "UniqueAdID1", "QYZ", "", 490, Condition.GOOD, "imgUrl", new ArrayList<>(), null));
        testData.add(AdFactory.createAd("22/10/19:10:15:00", "UniqueOwnerID", "UniqueAdID2", "DEF", "", 299, Condition.GOOD, "imgUrl", new ArrayList<>(), null));
        testAdvert = testData.get(0);
    }

    @Test
    public void bindingSetsNecessaryFields() {
        AdvertsPresenterHelper.onBindThumbnailViewAtPosition(0, viewMock, testData);

        verify(viewMock).setPrice(testAdvert.getPrice());
        verify(viewMock).setId(any());
        verify(viewMock).setTitle(testAdvert.getTitle());
    }

    @Test
    public void bindingIsNotMadeIfPositionIsLargerThanDataList() {
        AdvertsPresenterHelper.onBindThumbnailViewAtPosition(4, viewMock, testData);

        verify(viewMock, times(0)).setPrice(testAdvert.getPrice());
        verify(viewMock, times(0)).setId(any());
        verify(viewMock, times(0)).setTitle(testAdvert.getTitle());
    }
}

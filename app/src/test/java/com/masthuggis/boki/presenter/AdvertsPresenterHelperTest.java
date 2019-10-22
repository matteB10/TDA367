package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.view.ThumbnailView;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

public class AdvertsPresenterHelperTest {
    private List<Advertisement> testData;

    @Mock
    private ThumbnailView mockView;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private void createTestData() {
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("170101200000", "UniqueOwnerID", "UniqueAdID", "Title1","", 490, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("180101200000", "UniqueOwnerID", "UniqueAdID", "Title2","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("150101200000", "UniqueOwnerID", "UniqueAdID", "Title3","", 230, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("130101200000", "UniqueOwnerID", "UniqueAdID", "Title4","", 300, Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "longTitleID", "Very Specific Name For Testing Purposes","", 639, Condition.GOOD,"", new ArrayList<>(),null));
    }


}

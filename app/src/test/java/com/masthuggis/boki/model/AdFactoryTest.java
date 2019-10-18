package com.masthuggis.boki.model;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
public class AdFactoryTest {

    List<String> tags = new ArrayList<>();

    Advertisement advertisement = new Advert(
            "datePublished",
            "uniqueOwnerID",
            "id",
            "title",
            "description",
            123123,
            Advert.Condition.NEW,
            "imageUrl",
            new ArrayList<>(),
            "owner");

    @Mock
    private DataModel databaseMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void testCreateAdWithParameters() {

        tags.add("IT");
        tags.add("Data");
        tags.add("tjosvejs");
        Advertisement testAd = AdFactory.createAd(advertisement.getDatePublished(), advertisement.getUniqueOwnerID(), advertisement.getUniqueID(), advertisement.getTitle(), advertisement.getDescription(), advertisement.getPrice(), advertisement.getCondition(), advertisement.getImageUrl()
        ,advertisement.getTags(), advertisement.getOwner());
        assert (advertisement.getDatePublished().equals(testAd.getDatePublished()));
        assert (advertisement.getUniqueOwnerID().equals(testAd.getUniqueOwnerID()));
        assert (advertisement.getUniqueID().equals(testAd.getUniqueID()));
        assert (advertisement.getTitle().equals(testAd.getTitle()));
        assert (advertisement.getDescription().equals(testAd.getDescription()));
        assert (advertisement.getPrice() == testAd.getPrice());
        assert (advertisement.getCondition().toString().equals(testAd.getCondition().toString()));
        assert (advertisement.getOwner().equals(testAd.getOwner()));
        assert (advertisement.getTags().size() == testAd.getTags().size());
        for (int i = 0; i < advertisement.getTags().size(); i++) {
            assert (advertisement.getTags().get(i).equals(testAd.getTags().get(i)));
        }
    }

    //TODO fråga Iceman hur man löser detta fel
    @Test
    public void testCreateAd() {
        Mockito.doNothing().when(DataModel.getInstance());
        when(databaseMock.getUserID()).thenReturn("userID");
        when(databaseMock.getUserDisplayName()).thenReturn("userDisplayName");
        Advertisement testAd = AdFactory.createAd();
        Advert advert = new Advert();
        assert (advert.getDatePublished().equals(testAd.getDatePublished()));
        assert (advert.getUniqueOwnerID().equals(testAd.getUniqueOwnerID()));
        assert (advert.getUniqueID().equals(testAd.getUniqueID()));
        assert (advert.getTitle().equals(testAd.getTitle()));
        assert (advert.getDescription().equals(testAd.getDescription()));
        assert (advert.getPrice() == testAd.getPrice());
        assert (advert.getCondition().toString().equals(testAd.getCondition().toString()));
        assert (advert.getOwner().equals(testAd.getOwner()));
        assert (advert.getTags().size() == testAd.getTags().size());
        for (int i = 0; i < advert.getTags().size(); i++) {
            assert (advert.getTags().get(i).equals(testAd.getTags().get(i)));
        }

    }
}

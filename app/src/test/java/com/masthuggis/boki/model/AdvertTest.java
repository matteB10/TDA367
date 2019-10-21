package com.masthuggis.boki.model;

import com.masthuggis.boki.utils.Condition;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AdvertTest {

    List<String> tags = new ArrayList<>();


    Advertisement ad = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
            "description", 123, Condition.OK, "imageUrl", tags, "owner");



    @Test
    public void testGetDateTest() {
        assert (ad.getDatePublished().equals("datePublished"));
    }

    @Test
    public void getImageUrlTest() {
        assert (ad.getImageUrl().equals("imageUrl"));
    }

    @Test
    public void getTitleTest() {
        assert (ad.getTitle().equals("title"));
    }

    @Test
    public void getPriceTest() {
        assert (ad.getPrice() == 123);
    }

    @Test
    public void getUniqueOwnerIDTest() {
        assert (ad.getUniqueOwnerID().equals("uniqueOwnerID"));
    }

    @Test
    public void getDescriptionTest() {
        assert (ad.getDescription().equals("description"));
    }

    @Test
    public void getTagsTest() {
        assert (ad.getTags().size() == 0);
        tags.add("tag");
        assert (ad.getTags().size() == 1);
        assert (ad.getTags().get(0).equals("tag"));
        assert (ad.getTags() == tags);
    }

    @Test
    public void getConditionTest() {
        assert (ad.getCondition().toString().equals(Condition.OK.toString()));
    }

    @Test
    public void getUniqueIDTest() {
        assert (ad.getUniqueID().equals("advertID"));
    }

    @Test
    public void setTitleTest() {
        Advertisement ad = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
                "description", 123, Condition.OK, "imageUrl", tags, "owner");
        assert (!ad.getTitle().equals("Title set"));
        ad.setTitle("Title set");
        assert (ad.getTitle().equals("Title set"));
    }

    @Test
    public void setPriceTest() {
        Advertisement ad = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
                "description", 123, Condition.OK, "imageUrl", tags, "owner");
        assert (ad.getPrice() != 999);
        ad.setPrice(999);
        assert (ad.getPrice() == 999);
        ad.setPrice(123);
        assert (ad.getPrice() == 123);
        ad.setPrice(-1);
        assert (ad.getPrice() == 123);
    }

    @Test
    public void setDescriptionTest() {
        Advertisement ad = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
                "description", 123, Condition.OK, "imageUrl", tags, "owner");
        assert (!ad.getDescription().equals("Changed description"));
        ad.setDescription("Changed description");
        assert (ad.getDescription().equals("Changed description"));
        ad.setDescription(null);
        assert (ad.getDescription() == null);
    }

    @Test
    public void isNewTagTest() {
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        Advertisement ad = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
                "description", 123, Condition.OK, "imageUrl", tags, "owner");
        assert (!ad.isNewTag("tag1"));
        assert (ad.isNewTag("newTag"));
        tags.add("newTag");
        assert (!ad.isNewTag("newTag"));
        tags.remove("tag1");
        assert (ad.isNewTag("tag1"));
    }

    @Test
    public void tagsChangedTest() {
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        assert (tags.size() == 2);
        Advertisement ad = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
                "description", 123, Condition.OK, "imageUrl", tags, "owner");
        ad.toggleTag("tag1");
        assert (ad.getTags().size() == 1);
        assert (ad.getTags().get(0).equals("tag2"));
        ad.toggleTag("tag1");
        assert (ad.getTags().size() == 2);
        assert (ad.getTags().get(1).equals("tag1"));
    }

    @Test
    public void setDatePublishedTest() {
        Advertisement ad = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
                "description", 123, Condition.OK, "imageUrl", tags, "owner");
        assert (!ad.getDatePublished().equals("Changed datePublished"));
        ad.setDatePublished("Changed datePublished");
        assert (ad.getDatePublished().equals("Changed datePublished"));
    }

    @Test
    public void setConditionTest() {
        Advertisement ad = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
                "description", 123, Condition.OK, "imageUrl", tags, "owner");
        assert (ad.getCondition() == Condition.OK);
        ad.setCondition(Condition.NEW);
        assert (ad.getCondition() == Condition.NEW);
        ad.setCondition(Condition.GOOD);
        assert (ad.getCondition() == Condition.GOOD);
        ad.setCondition(Condition.OK);
        assert (ad.getCondition() == Condition.OK);
        ad.setCondition(Condition.OK);
        assert (ad.getCondition() == Condition.UNDEFINED);
    }

    @Test
    public void isValidConditionTest() {
        Advertisement ad = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
                "description", 123, Condition.OK, "imageUrl", tags, "owner");
        assert (ad.isValidCondition());
        //ad.setCondition(0);
       // assert (!ad.isValidCondition());
        ad.setCondition(Condition.NEW);
        assert (ad.isValidCondition());
    }

    @Test
    public void getOwnerTest() {
        Advertisement ad = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
                "description", 123, Condition.OK, "imageUrl", tags, "owner");

        assert (ad.getOwner().equals("owner"));
        Advert ad2 = new Advert("datePublished", "uniqueOwnerID", "advertID", "title",
                "description", 123, Condition.OK, "imageUrl", tags, "owner2");
        assert (!ad.getOwner().equals("owner2"));
        assert (ad2.getOwner().equals("owner2"));
        Advertisement ad3 = ad;
        assert (ad3.getOwner().equals(ad.getOwner()));
    }
}

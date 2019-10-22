package com.masthuggis.boki;

import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock class with hard coded books for testing purpose
 */

 public class MockRepository {
    private static List<Advertisement> adverts = new ArrayList<>();

    public static List<Advertisement> getTestAds() {
        createMockBooks();
        return new ArrayList<>(adverts);
    }

    private static void createMockBooks() {
        List<String> tags = new ArrayList<>();
        tags.add("IT/Data");
        adverts.add(AdFactory.createAd("01/01/18 10:12:55", "UniqueOwnerID", "AdID", "Diskret",
                "description", 180, Condition.GOOD, "", tags, ""));
        List<String> tags1 = new ArrayList<>();
        tags1.add("Envar");
        adverts.add(AdFactory.createAd("01/11/18 10:12:55", "UniqueOwnerID", "AdID", "Calculus",
                "description", 450, Condition.OK, "", tags1, ""));
        List<String> tags2 = new ArrayList<>();
        tags2.add("kemi");
        adverts.add(AdFactory.createAd("01/12/18 10:12:55", "UniqueOwnerID", "AdID", "Linjär Algebra",
                "description", 250, Condition.GOOD, "", tags2, ""));

        adverts.add(AdFactory.createAd("01/12/19 10:12:55", "UniqueOwnerID", "AdID", "Arkitektur",
                "description", 300, Condition.GOOD, "", tags, ""));

        adverts.add(AdFactory.createAd("02/01/19 10:12:55", "UniqueOwnerID", "AdID", "Design",
                "description", 250, Condition.OK, "", tags, ""));

        adverts.add(AdFactory.createAd("03/01/19 08:12:55", "UniqueOwnerID", "AdID", "Clean Code",
                "description", 200, Condition.GOOD, "", tags, ""));

    }
}

package com.masthuggis.boki;

import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.Condition;

import java.util.ArrayList;
import java.util.List;


public class MockRepository {
    private List<Advertisement> adverts = new ArrayList<>();
    private static MockRepository instance;

    private MockRepository() {
        createMockBooks();
    }


    public static MockRepository getInstance() {
        if (instance == null)
            instance = new MockRepository();
        return instance;
    }

    public List<Advertisement> getTestAds() {
        return new ArrayList<>(adverts);
    }
/*
    private void getMockDataOfAllAds() {
        try {
            JSONObject booksObject;
            JSONArray booksArray = booksObject.getJSONArray("books"); //Array in json file must be named "books"
            for (int i = 0; i < booksArray.length(); i++) {
                //Needs some way to create a book from the data that is fetched from each JSON-object
                createBookWithoutTags(booksArray.getJSONObject(i));
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    private Advertisement createBookWithoutTags(JSONObject object) {
        String title, date;
        int price;
        List<String> strTags = new ArrayList<>();
        Advert.Condition condition;
        try { //Should use a factory-method instead
            title = object.getString("title");
            price = object.getInt("price");
            date = object.getString("date");
            String conditionString = object.getString("condition");
            condition = Advert.Condition.valueOf(conditionString); //Necessary step as it otherwise tries to cast a String into a Condition

            JSONArray tags = (JSONArray) object.get("tags");
            System.out.print("tags:");
            for (int i = 0; i < tags.length(); i++) {
                strTags.add(tags.getJSONObject(i).toString());
            }

            Advertisement ad = AdFactory.createAd(date, "UniqueOwnerID", "UniqueAdID", title, "", price, condition, "", strTags, null);
            adverts.add(ad);
            return ad;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
    private void getMockJsonObj() {
        JSONParser jsonParser = new JSONParser();
        try {
            ClassLoader cl = getClass().getClassLoader();
            File file = new File(cl.getResource("app/src/test/java/com/masthuggis/boki/mockBooks.json").getFile());
            Object object = jsonParser.parse(new FileReader(file));

            JSONObject jsonObject = (JSONObject) object;
            JSONArray booksArray = jsonObject.getJSONArray("books");

            for (int i = 0; i < booksArray.length(); i++) {
                //Needs some way to create a book from the data that is fetched from each JSON-object
                createBookWithoutTags(booksArray.getJSONObject(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void createMockBooks() {
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

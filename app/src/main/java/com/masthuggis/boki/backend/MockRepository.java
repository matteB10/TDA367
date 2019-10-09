package com.masthuggis.boki.backend;

import android.content.Context;

import com.masthuggis.boki.Boki;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MockRepository {
    private List<Advertisement> adverts = new ArrayList<>();
    private static MockRepository instance;

    private void MockRepository() {}

    public static MockRepository getInstance() {
        if (instance == null)
            instance = new MockRepository();
        return instance;
    }

    public List<Advertisement> getLocalJSONAds() {
        adverts.clear();
        getMockDataOfAllAds();
        return new ArrayList<>(adverts);
    }

    private void getMockDataOfAllAds() {
        String json = getMockBooks(Boki.getAppContext());
        try {
            JSONObject booksObject = new JSONObject(json);
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
        Advert.Condition condition;
        try { //Should use a factory-method instead
            title = object.getString("title");
            price = object.getInt("price");
            date = object.getString("date");
            String conditionString = object.getString("condition");
            condition = Advert.Condition.valueOf(conditionString); //Necessary step as it otherwise tries to cast a String into a Condition
            Advertisement ad = AdFactory.createAd(date, "UniqueOwnerID", "UniqueAdID", title,"", price, condition,"",new ArrayList<String>(),null);
            adverts.add(ad);
            return ad;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getMockBooks(Context context) {
        String json;
        try {
            InputStream inputStream = context.getAssets().open("mockBooks.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            return json;

        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}

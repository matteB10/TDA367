package com.masthuggis.boki;


import com.masthuggis.boki.backend.BackendDataFetcher;
import com.masthuggis.boki.backend.BookRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryTest {


    @Test
    public void getBooks() {
        JSONObject booksJsonObj = initBookJsonObj();
        List<String> books = new ArrayList<>();

        try {
            JSONArray array = booksJsonObj.getJSONArray("books");
            JSONObject book = array.getJSONObject(0);
            String title = (String)book.get("title");
            System.out.println(title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static JSONObject initBookJsonObj() {
        try{
            String booksJson = BackendDataFetcher.readJsonFile(BackendDataFetcher.class.getResource("mockBooks.json"));
            return new JSONObject(booksJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }


}

package com.masthuggis.boki.backend;

import android.content.Context;

import com.masthuggis.boki.Boki;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class providing the functionality to convert data from backend into Book-objects to be used
 * by the domain-layer of the application.
 * Data is fetched using the BackendDataFetcher class.
 */

public class BookRepository {
    private static JSONObject booksJsonObj;

    /**
     * Method that fetches all books from the local .json.file and returns them as a list of Advert
     * objects. Returns a new list for every method call.
     *
     * @param context the Context of the activity used to access the json-file through the assets.
     * @return a list of all the Advert objects that have been created from the json-file.
     */

    public static List<Advert> getAllAdverts() {
        String json = BackendDataFetcher.loadJSONFromAsset(Boki.getAppContext());
        List<Advert> books = new ArrayList<>();
        try {
            JSONObject booksObject = new JSONObject(json);
            JSONArray booksArray = booksObject.getJSONArray("books"); //Array in json file must be named "books"
            for (int i = 0; i < booksArray.length(); i++) {
                //Needs some way to create a book from the data that is fetched from each JSON-object
                books.add(createBook(booksArray.getJSONObject(i)));
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return books;
    }


    /**
     * Creates a book object given a JSONObject from the local file.
     *
     * @param object the JSONObject used to create a Book-object
     * @return a Book with fields corresponding to the key-value pairs of the JSONObject.
     */
    private static Book createBook(JSONObject object) {
        String title;
        String author;
        int edition;
        int price;
        long isbn;
        int yearPublished;
        Book.Condition condition;
        List<String> preDefinedTags;
        List<String> userTags;
        try {
            title = object.getString("title");
            author = object.getString("author");
            edition = object.getInt("edition");
            price = object.getInt("price");
            isbn = object.getInt("isbn");
            yearPublished = object.getInt("yearPublished");
            condition = Book.Condition.valueOf(object.getString("condition")); //Shorthand
            preDefinedTags = getPreDefinedTags(object);
            userTags = getUserTags(object);
            return new Book("01/01/19",title, price, author, edition, isbn, condition, preDefinedTags, userTags);
        } catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }


    private static List<String> getPreDefinedTags(JSONObject object) {
        try {
            JSONArray tagsArray = object.getJSONArray("preDefinedTags");
            List<String> preDefinedTags = new ArrayList<>();
            for (int i = 0; i < tagsArray.length(); i++) {
                preDefinedTags.add(tagsArray.getString(i));
            }
            return preDefinedTags;
        } catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }


    private static List<String> getUserTags(JSONObject object) {
        try {
            JSONArray tagsArray = object.getJSONArray("userTags");
            List<String> userTags = new ArrayList<>();
            for (int i = 0; i < tagsArray.length(); i++) {
                userTags.add(tagsArray.getString(i));
            }
            return userTags;
        } catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}




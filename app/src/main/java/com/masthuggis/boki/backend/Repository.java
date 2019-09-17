package com.masthuggis.boki.backend;

import android.content.Context;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.Book;
import com.masthuggis.boki.model.User;

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

public class Repository implements iRepository {
    private static JSONObject booksJsonObj;
    private static Repository singleton = null;


    private Repository() {
    }

    public static Repository getInstance() {
        if (singleton == null) {
            return new Repository();
        } else {
            return singleton;
        }
    }

    /**
     * Method that fetches all books from the local .json.file and returns them as a list of book
     * objects. Might be smarter to move the books-variable and make it a local variable for
     * the sake of performance.
     *
     * @param context the Context of the activity used to access the json-file through the assets.
     * @return a list of all the book objects that have been created from the json-file.
     */

    @Override
    public List<Advertisement> getAllAds(Context context) {
        String json = BackendDataFetcher.getInstance().getMockBooks(context);
        List<Advertisement> books = new ArrayList<>();
        try {
            JSONObject booksObject = new JSONObject(json);
            JSONArray booksArray = booksObject.getJSONArray("books"); //Array in json file must be named "books"
            for (int i = 0; i < booksArray.length(); i++) {
                //Needs some way to create a book from the data that is fetched from each JSON-object
                books.add(createBookWithoutTags(booksArray.getJSONObject(i)));
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return books;
    }

    /**
     * Should probably use som form of factory for creating books in order to make the method
     * call less tedious.
     * Does NOT create book-objects with tags in this version, user defined or not, DO NOT USE
     * if all fields of the Book are required, faster performance-wise than creating a full
     * Book-object with related tags.
     *
     * @param object the JSON-object which key-value pairs are read and converted into
     *               the fields of the new Book-object.
     */

    private Advertisement createBookWithoutTags(JSONObject object) {
        String title;
        String author;
        int edition;
        int price;
        long isbn;
        int yearPublished;
        Book.Condition condition;
        try { //Should use a factory-method instead
            title = object.getString("title");
            author = object.getString("author");
            edition = object.getInt("edition");
            price = object.getInt("price");
            isbn = object.getLong("isbn");
            yearPublished = object.getInt("yearPublished");
            String conditionString = object.getString("condition");
            condition = Book.Condition.valueOf(conditionString); //Necessary step as it otherwise tries to cast a String into a Condition
            return AdFactory.createAd(123, "test date", title, author, edition, isbn, yearPublished, price, condition, null, null);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a book object given a JSONObject from the local file.
     * This version DOES create a Book complete with both User defined and preDefined Tags,
     * might impact performance.
     *
     * @param object the JSONObject used to create a Book-object
     * @return a Book with fields corresponding to the key-value pairs of the JSONObject.
     */
    @Override
    public Advertisement createAdvert(JSONObject object) {
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
            isbn = object.getLong("isbn");
            yearPublished = object.getInt("yearPublished");
            condition = Book.Condition.valueOf(object.getString("condition")); //Shorthand
            preDefinedTags = getPreDefinedTags(object);
            userTags = getUserTags(object);
            return AdFactory.createAd(123, "test date", title, author, edition, isbn, yearPublished, price, condition, preDefinedTags, userTags);
        } catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public User createUser() {
        return new User();
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




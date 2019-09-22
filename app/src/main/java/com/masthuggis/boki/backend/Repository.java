package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.Boki;
import com.masthuggis.boki.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.google.gson.internal.bind.TypeAdapters.UUID;

/**
 * Class providing the functionality to convert data from backend into Book-objects to be used
 * by the domain-layer of the application.
 * Data is fetched using the BackendDataFetcher class.
 */

public class Repository implements RepositoryObserver {
    private static JSONObject booksJsonObj;
    private static Repository singleton = null;
    private final List<Advertisement> temporaryListOfAllAds = new ArrayList<>();
    private final List<RepositoryObserver> observers = new ArrayList<>();


    private Repository() {
    }

    public static Repository getInstance() {
        if (singleton == null) {
            singleton = new Repository();
        }
        return singleton;

    }

    /**
     * Method that fetches all books from the local .json.file and returns them as a list of Advert
     * objects. Returns a new list for every method call.
     *
     * @return a list of all the Advert objects that have been created from the json-file.
     */


    public List<Advertisement> getAllAds() {
        String json = BackendDataFetcher.getInstance().getMockBooks(Boki.getAppContext());
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
        Advert.Condition condition;
        try { //Should use a factory-method instead
            title = object.getString("title");
            author = object.getString("author");
            edition = object.getInt("edition");
            price = object.getInt("price");
            isbn = object.getLong("isbn");
            yearPublished = object.getInt("yearPublished");
            String conditionString = object.getString("condition");
            condition = Advert.Condition.valueOf(conditionString); //Necessary step as it otherwise tries to cast a String into a Condition
            List<String> imgURLS = new ArrayList<>();
            Advertisement ad = AdFactory.createAd(new Date(19, 9, 18), "UniqueOwnerID", title, imgURLS, "Description", price, condition);
            temporaryListOfAllAds.add(ad);
            return ad;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a book object given a JSONObject from the local file.
     *
     * @param object the JSONObject used to create a Book-object
     * @return a Book with fields corresponding to the key-value pairs of the JSONObject.
     */
    public Advertisement createAdvert(JSONObject object) {
        String title;
        String author;
        int edition;
        int price;
        long isbn;
        int yearPublished;
        Advert.Condition condition;
        List<String> preDefinedTags;
        List<String> userTags;
        try {
            title = object.getString("title");
            author = object.getString("author");
            edition = object.getInt("edition");
            price = object.getInt("price");
            isbn = object.getLong("isbn");
            yearPublished = object.getInt("yearPublished");
            condition = Advert.Condition.valueOf(object.getString("condition")); //Shorthand
            preDefinedTags = getPreDefinedTags(object);
            userTags = getUserTags(object);
            List<String> imgURLS = new ArrayList<>();
            Advertisement ad = AdFactory.createAd(new Date(19, 9, 18), "UniqueOwnerID", title, imgURLS, "Description", price, condition);
            temporaryListOfAllAds.add(ad);
            userAdvertsForSaleUpdate(temporaryListOfAllAds.iterator());
            return ad;
        } catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Creates an advertisement given input from createAdvertPresenter
     *
     * @param t,d,p,c needed to create advert
     */
    public void createAdvert(String t, String d, int p, Advert.Condition c, List<String> tags, String imageURL) {
        Advertisement ad = AdFactory.createAd(new Date(19, 9, 18), "UniqueOwnerID", t, imageURL, "Description", p, c);
        temporaryListOfAllAds.add(ad);
        userAdvertsForSaleUpdate(temporaryListOfAllAds.iterator());
    }

    public List<Advertisement> getTemporaryListOfAllAds() {
        return temporaryListOfAllAds;
    }

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

    /**
     * Temporary method for getting ad from unique ID.
     *
     * @param
     * @return
     */
    public Advertisement getAdFromId(String name) {

        for (Advertisement ad : temporaryListOfAllAds) {
            if (ad.getTitle().equals(name)) {
                return ad;
            }
        }
        return null;
    }

    public void addObserver(RepositoryObserver observer) {
        observers.add(observer);
    }

    @Override
    public void userAdvertsForSaleUpdate(Iterator<Advertisement> advertsForSale) {
        // TODO: change from temp list to actual user list
        observers.forEach(observer -> observer.userAdvertsForSaleUpdate(getTemporaryListOfAllAds().iterator()));
    }
}




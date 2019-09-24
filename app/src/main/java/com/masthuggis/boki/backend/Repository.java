package com.masthuggis.boki.backend;

import com.masthuggis.boki.Boki;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class providing the functionality to convert data from backend into Book-objects to be used
 * by the domain-layer of the application.
 * Data is fetched using the BackendDataFetcher class.
 */

public class Repository implements RepositoryObserver {
    private static JSONObject booksJsonObj;
    private static Repository repository;
    private final List<Advertisement> temporaryListOfAllAds = new ArrayList<>();
    private final List<RepositoryObserver> observers = new ArrayList<>();

    private Repository() {
    }

    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }


    /**
     * Method that fetches all books from the local .json.file and returns them as a list of Advert
     * objects. Returns a new list for every method call.
     *
     * @return a list of all the Advert objects that have been created from the json-file.
     */

    private void readFromBackend() {
        String json = BackendDataFetcher.getInstance().getMockBooks(Boki.getAppContext());
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

    /**
     * Used during developement, just needed to be called once. Reads from json
     * and adds Adverts to temporary list.
     *
     * @return
     */

    public List<Advertisement> getAllAds() {
        //Only during developement, hard coded value from number of ads in Json-file
        if (temporaryListOfAllAds.size() < 12) {
            readFromBackend();
        }
        return temporaryListOfAllAds;
    }

    /**
     * Should be used for now instead of getAllAdds()
     *
     * @return temporaryListOfAllAds
     */
    public List<Advertisement> getTemporaryListOfAllAds() {
        return temporaryListOfAllAds;
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
            Advertisement ad = AdFactory.createAd(new Date(19, 9, 18), "UniqueOwnerID", "UniqueAdID", title, imgURLS, "Description", price, condition);
            temporaryListOfAllAds.add(ad);
            return ad;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Creates a new Advertisement given input from user
     */
    public void storeAdvertInFirebase(String userID, String adID, String title, String description, int price, Advert.Condition condition, List<String> tags, String imageURL) {
        Advertisement ad = AdFactory.createAd(new Date(19, 9, 18), userID, adID, title, imageURL, description, price, condition);
        userAdvertsForSaleUpdate(temporaryListOfAllAds.iterator());

        temporaryListOfAllAds.add(ad);
    }

    /**
     * Creates a new empty Advertisement, used during creation of a new ad
     *
     * @return an empty Advertisement
     */
    public Advertisement storeAdvertInFirebase() {
        return AdFactory.createAd();

    }


    /**
     * @param advertisement gets saved into temporary list
     */
    public void saveAdvert(Advertisement advertisement) {
        temporaryListOfAllAds.add(advertisement);
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
     * Method for getting ad from unique ID.
     *
     * @param
     * @return
     */
    public Advertisement getAdFromId(String id) {

        for (Advertisement ad : temporaryListOfAllAds) {
            if (ad.getUniqueID().equals(id)) {
                return ad;
            }
        }
        return null;
    }

    //THIS METHOD CREATES AN ADVERT IN THE FIRESTORE DATABASE
    //Arguments should be fetched from fields when creating advert
    public void storeAdvertInFirebase(Date datePublished, String uniqueOwnerID, String title, List<String> imgURLs,
                                      String description, int price, Advert.Condition condition, List<String> tags) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("description", description);
        data.put("uniqueOwnerID", uniqueOwnerID);
        data.put("condition", condition);
        data.put("price", price);
        data.put("imageURLs", imgURLs);
        data.put("tags", tags);
        BackendDataFetcher.getInstance().writeAdvertToFirebase(data);
    }


    //Fetches adverts from a user given a specific userID
    //Uses custom interface for a callback from async query in the firebase db.
    public void fetchAdvertsFromUserID(String userID, advertisementCallback advertisementCallback) {
        List<Advertisement> userIDAdverts = new ArrayList<>();
        BackendDataFetcher.getInstance().readFirebaseData(new advertisementDBCallback() {
            @Override
            public void onCallBack(List<Map<String, Object>> advertDataList) {
                for (Map<String, Object> dataMap : advertDataList) {
                    userIDAdverts.add(retrieveAdvert(dataMap,userID));
                }
                advertisementCallback.onCallback(userIDAdverts);
            }
        }, userID);

    }


    public void fetchAllAdverts(advertisementCallback advertisementCallback) {

    }


    //Creates an Advert object with data given in form of a Key-Value Map
    //Very hardcoded, "Magic-string" implementation, but should be okay
    private Advertisement retrieveAdvert(Map<String, Object> dataMap, String uniqueUserID) {
        //Date datePublished = dataMap.get("datePublished"); //Oklart
        String title = (String) dataMap.get("title");
        String description = (String) dataMap.get("description");
        long price = (long) dataMap.get("price");
        List<String> imgURLs = (List<String>) dataMap.get("imageURLs");
        List<String> tags = (List<String>) dataMap.get("tags");
        //TODO implement Date and Condition into Firebase in a neat fashion
        return AdFactory.createAd(null, uniqueUserID, "uniqueAdID", title, imgURLs, description, 23, null);

    }


    //TODO FOR LATER IMPLEMENTATION
    public void addObserver(RepositoryObserver observer) {
        observers.add(observer);
    }

    @Override
    public void userAdvertsForSaleUpdate(Iterator<Advertisement> advertsForSale) {
        // TODO: change from temp list to actual user list
        observers.forEach(observer -> observer.userAdvertsForSaleUpdate(getTemporaryListOfAllAds().iterator()));
    }
}




package com.masthuggis.boki.backend;

import com.masthuggis.boki.Boki;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class providing the functionality to convert data from backend into Book-objects to be used
 * by the domain-layer of the application.
 * Data is fetched using the BackendDataHandler class.
 */

public class Repository {
    private static Repository repository;
    private final List<RepositoryObserver> observers = new ArrayList<>();
    private List<Advertisement> allAds = new ArrayList<>();

    private Repository() {
    }


    //Make repository generate a mock userID
    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    /**
     * Creates a new empty Advertisement, used during creation of a new ad
     *
     * @return an empty Advertisement
     */
    public Advertisement createAdvert() {
        return AdFactory.createAd();
    }

    /**
     * @param advertisement gets saved into temporary list as well as in firebase
     */

    //TODO implement functionality for uploading the image of Advert to Firebase
    public void saveAdvert(Advertisement advertisement, File imageFile) {
        allAds.add(advertisement); //Saves in a temporary list
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("title", advertisement.getTitle());
        dataMap.put("description", advertisement.getDescription());
        dataMap.put("uniqueOwnerID", advertisement.getUniqueOwnerID());
        dataMap.put("condition", advertisement.getCondition());
        dataMap.put("price", advertisement.getPrice());
        dataMap.put("tags", advertisement.getTags());
        dataMap.put("uniqueAdID", advertisement.getUniqueID());
        dataMap.put("date", advertisement.getDatePublished());
        BackendDataHandler.getInstance().writeAdvertToFirebase(dataMap, imageFile);
    }

    //Same functionality as above method but based off of firebase
    public Advertisement getAdFromAdID(String ID) {
        for (Advertisement ad : allAds) {
            if (ad.getUniqueID().equals(ID))
                return ad;
        }
        return null; //TODO Fix a better solution to handle NPExc....
    }

    /**
     * Gets all adverts in firebase that belong to a specific userID
     * userID can be any string that isn't null nor an empty string
     */
    public void fetchAdvertsFromUserIDFirebase(String userID, advertisementCallback advertisementCallback) {
        List<Advertisement> userIDAdverts = new ArrayList<>();
        BackendDataHandler.getInstance().readUserIDAdverts(new advertisementDBCallback() {
            @Override
            public void onCallBack(List<Map<String, Object>> advertDataList) {
                for (Map<String, Object> dataMap : advertDataList) {
                    userIDAdverts.add(retrieveAdvertWithUserID(dataMap, userID));
                }
                advertisementCallback.onCallback(userIDAdverts);
            }
        }, userID);
    }

    private void fetchAllAdverts(advertisementCallback advertisementCallback) {
        BackendDataHandler.getInstance().readAllAdvertData(advertDataList -> {
            allAds.clear();
            for (Map<String, Object> dataMap : advertDataList) { //Loop runs twice, shouldn't be the case
                allAds.add(retrieveAdvert(dataMap));
            }
            advertisementCallback.onCallback(allAds); //Application lands here three times, making the list 3x bigger than it should
        });
    }

    public String getFireBaseID(String userID, String advertID) {
        return BackendDataHandler.getInstance().getFireBaseID(userID, advertID);
    }

    //TODO FOR LATER IMPLEMENTATION
    public void addObserver(RepositoryObserver observer) {
        observers.add(observer);
    }

    public void getAllAds(advertisementCallback advertisementCallback) {
        // If there are adverts already stored, return those, else make a request. The stored
        // adverts, if there are any, will be same as the ones stored on the database.
        // TODO: make a setup so it does not have to do fetch every time (only if necessary)
        fetchAllAdverts(advertisementCallback);
    }

    public List<Advertisement> getLocalJSONAds() {
        allAds.clear();
        getMockDataOfAllAds();
        return allAds;
    }

    /**
     * Retrieves local mock JSON file for debugging purposes.
     */
    public void getMockDataOfAllAds() {
        String json = BackendDataHandler.getInstance().getMockBooks(Boki.getAppContext());
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
            Advertisement ad = AdFactory.createAd("", "UniqueOwnerID", "UniqueAdID", title, "imgURL", price, condition, new File("", ""));
            allAds.add(ad);
            return ad;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void notifyUsersAdvertsForSaleUpdated() {
        // TODO: change from temp list to actual user list
        //observers.forEach(observer -> observer.userAdvertsForSaleUpdate(updatedListIterator));
    }

    private void notifyMarketObservers() {
        observers.forEach(observer -> observer.allAdvertsInMarketUpdate(allAds.iterator()));
    }

    private Advertisement retrieveAdvert(Map<String, Object> dataMap) {
        String title = (String) dataMap.get("title");
        String description = (String) dataMap.get("description");
        long price = (long) dataMap.get("price");
        List<String> tags = (List<String>) dataMap.get("tags");
        String uniqueOwnerID = (String) dataMap.get("uniqueOwnerID");
        Advert.Condition condition = Advert.Condition.valueOf((String) dataMap.get("condition"));
        String uniqueAdID = (String) dataMap.get("uniqueAdID");
        String datePublished = (String) dataMap.get("date");
        File imageFile = (File) dataMap.get("imgFile");
        return AdFactory.createAd(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition, imageFile);
    }

    /**
     * Creates an Advertisement-object with AdFactory given a Key-Value map of the data required and a specific uniqueUserID
     * Called from fetchAdvertsFromUserID
     */
    private Advertisement retrieveAdvertWithUserID(Map<String, Object> dataMap, String uniqueOwnerID) {
        String title = (String) dataMap.get("title");
        String description = (String) dataMap.get("description");
        long price = (long) dataMap.get("price");
        List<String> tags = (List<String>) dataMap.get("tags");
        Advert.Condition condition = Advert.Condition.valueOf((String) dataMap.get("condition"));
        String uniqueAdID = (String) dataMap.get("uniqueAdID");
        String datePublished = (String) dataMap.get("date");
        return AdFactory.createAd(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition, null);
    } //TODO den här kommer behöva en imageFile den här med

}




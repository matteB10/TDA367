package com.masthuggis.boki.backend;

import android.util.Log;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

import java.io.File;
import java.io.InputStream;
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
    private static final List<RepositoryObserver> observers = new ArrayList<>();
    private static List<Advertisement> allAds = new ArrayList<>();

    private Repository() {
    }


    /**
     * Fetches data from Firebase and updates local list of Advertisements. Also notifies observers.
     */
    public static void updateAdverts() {
        //this.notifyMarketObservers();
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
    public static void saveAdvert(File imageFile, Advertisement advertisement) {
        DataModel.getInstance().addAdvertisement(advertisement);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("title", advertisement.getTitle());
        dataMap.put("description", advertisement.getDescription());
        dataMap.put("uniqueOwnerID", advertisement.getUniqueOwnerID());
        dataMap.put("condition", advertisement.getCondition());
        dataMap.put("price", advertisement.getPrice());
        dataMap.put("tags", advertisement.getTags());
        dataMap.put("uniqueAdID", advertisement.getUniqueID());
        dataMap.put("date", advertisement.getDatePublished());
        BackendDataHandler.getInstance().writeAdvertToFirebase(imageFile, dataMap, () -> Repository.notifyMarketObservers());
    }

    /**
     * Gets all adverts in firebase that belong to a specific userID
     * userID can be any string that isn't null nor an empty string
     */
    public void fetchAdvertsFromUserIDFirebase(String userID, advertisementCallback advertisementCallback) {
        List<Advertisement> userIDAdverts = new ArrayList<>();
        BackendDataHandler.getInstance().readUserIDAdverts(advertDataList -> {
            for (Map<String, Object> dataMap : advertDataList) {
                userIDAdverts.add(retrieveAdvertWithUserID(dataMap, userID));
            }
            advertisementCallback.onCallback(userIDAdverts);
        }, userID);
    }

    public static void fetchAllAdverts(advertisementCallback advertisementCallback) {
        Thread thread = new Thread(() -> BackendDataHandler.getInstance().readAllAdvertData(advertDataList -> {
            allAds.clear();
            for (Map<String, Object> dataMap : advertDataList) {
                allAds.add(retrieveAdvert(dataMap));
            }
            advertisementCallback.onCallback(allAds);
        }));
        thread.start();

    }

    public String getFireBaseID(String userID, String advertID) {
        return BackendDataHandler.getInstance().getFireBaseID(userID, advertID);
    }

    public static void addRepositoryObserver(RepositoryObserver observer) {
        observers.add(observer);
    }

    private static void notifyMarketObservers() {
        if (!Repository.observers.isEmpty())
            observers.forEach(observer -> observer.advertsInMarketUpdate(allAds));
    }

    public static void getAllAds(advertisementCallback advertisementCallback) {
        // If there are adverts already stored, return those, else make a request. The stored
        // adverts, if there are any, will be same as the ones stored on the database.
        // TODO: make a setup so it does not have to do fetch every time (only if necessary)
        fetchAllAdverts(advertisementCallback);
    }

    private static Advertisement retrieveAdvert(Map<String, Object> dataMap) {
        String title = (String) dataMap.get("title");
        String description = (String) dataMap.get("description");
        long price = (long) dataMap.get("price");
        List<String> tags = (List<String>) dataMap.get("tags");
        String uniqueOwnerID = (String) dataMap.get("uniqueOwnerID");
        Advert.Condition condition = Advert.Condition.valueOf((String) dataMap.get("condition"));
        String uniqueAdID = (String) dataMap.get("uniqueAdID");
        String datePublished = (String) dataMap.get("date");
        File imageFile = (File) dataMap.get("imgFile");
        String imageUrl = (String) dataMap.get("imgUrl");
        return AdFactory.createAd(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition, imageFile, imageUrl, tags);
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
        return AdFactory.createAd(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition, null, null, tags);
    } //TODO den här kommer behöva en imageFile den här med

}




package com.masthuggis.boki.backend;

import com.masthuggis.boki.backend.callbacks.FavouriteIDsCallback;
import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.model.observers.BackendObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class providing the functionality to convert data from backend into objects to be used
 * by the domain-layer of the application.
 * Data is fetched through the iBackend interface.
 * For methods that are delegating, check the class which is delegated to for comments.
 */
class AdvertRepository {

    private final iBackend backend;

    AdvertRepository(iBackend backend) {
        this.backend = backend;
    }


    /**
     * Converts an advertisement to a datamap which contains all information needed to later
     * create a new advertisement.
     *
     * @param advertisement gets saved into firebase
     * @param imageFile     image needed to later create URL for a remote storage location.
     */

    void saveAdvert(File imageFile, Advertisement advertisement) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("title", advertisement.getTitle());
        dataMap.put("description", advertisement.getDescription());
        dataMap.put("uniqueOwnerID", advertisement.getUniqueOwnerID());
        dataMap.put("condition", advertisement.getCondition());
        dataMap.put("price", advertisement.getPrice());
        dataMap.put("tags", advertisement.getTags());
        dataMap.put("uniqueAdID", advertisement.getUniqueID());
        dataMap.put("date", advertisement.getDatePublished());
        dataMap.put("advertOwner", advertisement.getOwner());
        backend.writeAdvertToFirebase(imageFile, dataMap);

    }


    void initialAdvertFetch(advertisementCallback advertisementCallback) {
        Thread thread = new Thread(() -> {
            backend.initialAdvertFetch(advertDataList -> {
                extractAdvertsFromData(advertisementCallback, advertDataList);
            });
        });
        thread.start();
    }

    void attachMarketListener(advertisementCallback advertisementCallback) {
        Thread thread = new Thread(() -> backend.attachMarketListener(advertDataList -> {
            extractAdvertsFromData(advertisementCallback, advertDataList);
        }));
        thread.start();
    }

    /**
     * Returns a List of advertisements after converting data to advertisements through a callback. This is done so that
     * the model isn't updated until the fetch is done.
     *
     * @param advertDataList contains the data needed to create new advertisements.
     */
    private void extractAdvertsFromData(advertisementCallback advertisementCallback, List<Map<String, Object>> advertDataList) {
        List<Advertisement> allAds = new ArrayList<>();
        for (Map<String, Object> dataMap : advertDataList) {
            allAds.add(retrieveAdvert(dataMap));
        }
        advertisementCallback.onCallback(allAds);
    }

    /**
     * Converts a dataMap into advertisements through a factory.
     *
     * @param dataMap a Map containing all information needed to create an advertisement
     * @return an advertisement.
     */
    private Advertisement retrieveAdvert(Map<String, Object> dataMap) {
        String title = "" + dataMap.get("title");
        String description = (String) dataMap.get("description");
        long price = (long) dataMap.get("price");
        List<String> tags = (List<String>) dataMap.get("tags");
        String uniqueOwnerID = (String) dataMap.get("uniqueOwnerID");
        Condition condition = Condition.valueOf((String) dataMap.get("condition"));
        String uniqueAdID = (String) dataMap.get("uniqueAdID");
        String datePublished = (String) dataMap.get("date");
        String owner = (String) dataMap.get("advertOwner");
        String imageUrl = (String) dataMap.get("imgUrl");
        return AdFactory.createAd(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition, imageUrl, tags, owner);
    }

    void getUserFavourites(String userID, FavouriteIDsCallback favouriteIDsCallback) {
        backend.getFavouriteIDs(userID, dataMap -> favouriteIDsCallback.onCallback((List<String>) dataMap.get("favourites")));
    }

    void deleteAd(List<Map<String, String>> chatReceiverAndUserIDMap, Map<String, String> adIDAndUserID) {
        backend.deleteAd(chatReceiverAndUserIDMap, adIDAndUserID);
    }


    void addBackendObserver(BackendObserver backendObserver) {
        backend.addBackendObserver(backendObserver);
    }

    void removeBackendObserver(BackendObserver backendObserver) {
        backend.removeBackendObserver(backendObserver);
    }

    void addToFavourites(String adID, String userID) {
        backend.addAdToFavourites(adID, userID);
    }


    void removeAdFromFavourites(String adID, String userID) {
        backend.removeAdFromFavourites(adID, userID);
    }


    void updateAdvert(File imageFile, Advertisement advertisement) {
        Map<String, Object> dataMap = new HashMap<>();
        mapAdData(dataMap, advertisement);
        backend.updateAdToFirebase(imageFile, dataMap);
    }

    /**
     * Adds all data from an advertisement to a map.
     * @param dataMap
     * @param advertisement
     */
    private void mapAdData(Map<String, Object> dataMap, Advertisement advertisement) {
        dataMap.put("title", advertisement.getTitle());
        dataMap.put("description", advertisement.getDescription());
        dataMap.put("uniqueOwnerID", advertisement.getUniqueOwnerID());
        dataMap.put("condition", advertisement.getCondition());
        dataMap.put("price", advertisement.getPrice());
        dataMap.put("tags", advertisement.getTags());
        dataMap.put("uniqueAdID", advertisement.getUniqueID());
        dataMap.put("date", advertisement.getDatePublished());
        dataMap.put("advertOwner", advertisement.getOwner());
    }

}




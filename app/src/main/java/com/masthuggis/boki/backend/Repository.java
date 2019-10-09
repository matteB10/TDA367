package com.masthuggis.boki.backend;

import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class providing the functionality to convert data from backend into objects to be used
 * by the domain-layer of the application.
 * Data is fetched through the iBackend interface.
 */
public class Repository {

    private final iBackend backend;

    Repository(iBackend backend) {
        this.backend = backend;
    }


    /**
     * @param advertisement gets saved into temporary list as well as in firebase
     */

    public void saveAdvert(File imageFile, Advertisement advertisement) {
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
        dataMap.put("advertOwner", advertisement.getOwner());
        backend.writeAdvertToFirebase(imageFile, dataMap, null);

    }

    /**
     * Gets all adverts in firebase that belong to a specific userID
     * userID can be any string that isn't null nor an empty string
     */
    public void fetchAdvertsFromUserIDFirebase(String userID, advertisementCallback advertisementCallback) {
        List<Advertisement> userIDAdverts = new ArrayList<>();
        backend.readUserIDAdverts(advertDataList -> {
            for (Map<String, Object> dataMap : advertDataList) {
                userIDAdverts.add(retrieveAdvertWithUserID(dataMap, userID));
            }
            advertisementCallback.onCallback(userIDAdverts);
        }, userID);
    }

    public void fetchAllAdverts(advertisementCallback advertisementCallback) {
        Thread thread = new Thread(() -> backend.readAllAdvertData(advertDataList -> {
            List<Advertisement> allAds = new ArrayList<>();
            for (Map<String, Object> dataMap : advertDataList) {
                allAds.add(retrieveAdvert(dataMap));
            }
            advertisementCallback.onCallback(allAds);
        }));
        thread.start();

    }

    public String getFireBaseID(String userID, String advertID) {
        return backend.getFireBaseID(userID, advertID);
    }

    public void getAllAds(advertisementCallback advertisementCallback) {
        // If there are adverts already stored, return those, else make a request. The stored
        // adverts, if there are any, will be same as the ones stored on the database.
        // TODO: make a setup so it does not have to do fetch every time (only if necessary)
        fetchAllAdverts(advertisementCallback);
    }

    private Advertisement retrieveAdvert(Map<String, Object> dataMap) {
        String title = "" + (String) dataMap.get("title");
        String description = (String) dataMap.get("description");
        long price = (long) dataMap.get("price");
        List<String> tags = (List<String>) dataMap.get("tags");
        String uniqueOwnerID = (String) dataMap.get("uniqueOwnerID");
        Advert.Condition condition = Advert.Condition.valueOf((String) dataMap.get("condition"));
        String uniqueAdID = (String) dataMap.get("uniqueAdID");
        String datePublished = (String) dataMap.get("date");
        String owner = (String) dataMap.get("advertOwner");
        String imageUrl = (String) dataMap.get("imgUrl");
        return AdFactory.createAd(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition, imageUrl, tags, owner);
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
        String owner = (String) dataMap.get("advertOwner");
        return AdFactory.createAd(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition, null, tags, owner);
    }

    public void deleteAd(String uniqueID) {

    }

    public void editTitle(String adID, String newTitle) {
    }

    public void editPrice(String adID, String newPrice) {
    }

    public void editDescription(String adID, String newDescription) {
    }

    public void addObserverToBackend(DataModel dataModel) {
        backend.addBackendObserver(dataModel);
    }
}




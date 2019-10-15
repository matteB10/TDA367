package com.masthuggis.boki.backend;

import com.masthuggis.boki.backend.callbacks.DBMapCallback;
import com.masthuggis.boki.backend.callbacks.FavouriteIDsCallback;
import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
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
 */
public class Repository {

    private final iBackend backend;
    private final DataModel dataModel;

    Repository(iBackend backend, DataModel dataModel) {
        this.backend = backend;
        this.dataModel = dataModel;
    }


    /**
     * @param advertisement gets saved into temporary list as well as in firebase
     */

    public void saveAdvert(File imageFile, Advertisement advertisement) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap = mapAdData(dataMap,advertisement);
        backend.writeAdvertToFirebase(imageFile, dataMap);
    }
    public void updateAdvert(File imageFile, Advertisement advertisement){
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap = mapAdData(dataMap,advertisement);
        backend.updateAdToFirebase(imageFile, dataMap);
    }

    private HashMap<String, Object> mapAdData(HashMap dataMap, Advertisement advertisement){
        dataMap.put("title", advertisement.getTitle());
        dataMap.put("description", advertisement.getDescription());
        dataMap.put("uniqueOwnerID", advertisement.getUniqueOwnerID());
        dataMap.put("condition", advertisement.getCondition());
        dataMap.put("price", advertisement.getPrice());
        dataMap.put("tags", advertisement.getTags());
        dataMap.put("uniqueAdID", advertisement.getUniqueID());
        dataMap.put("date", advertisement.getDatePublished());
        dataMap.put("advertOwner", advertisement.getOwner());
        return dataMap;
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


    public void getUserFavourites(FavouriteIDsCallback favouriteIDsCallback) {
        backend.getFavouriteIDs(new DBMapCallback() {
            @Override
            public void onCallBack(Map<String, Object> dataMap) {
                favouriteIDsCallback.onCallback((List<String>) dataMap.get("favourites"));
            }
        });
    }

    private Advertisement retrieveAdvert(Map<String, Object> dataMap) {
        String title = "" + dataMap.get("title");
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


    public void deleteAd(Advertisement advertisement) {
        backend.deleteAd(advertisement.getUniqueID());
    }


    public void addBackendObserver(BackendObserver backendObserver) {
        backend.addBackendObserver(backendObserver);
    }
    public void removeBackendObserver(BackendObserver backendObserver) {
        backend.removeBackendObserver(backendObserver);
    }


    public void addToFavourites(String adID, String userID) {
        backend.addAdToFavourites(adID, userID);
    }

    public void removeFromFavourites(String adID, String userID) {
        backend.removeAdFromFavourites(adID, userID);
    }
}




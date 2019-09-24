package com.masthuggis.boki.backend;


import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.User;
import org.json.JSONObject;
import java.util.Date;
import java.util.List;

public interface iRepository {
    Advertisement createAdvert(JSONObject jsonObject);
    User createUser();
    List<Advertisement> getAllAds();
    Advertisement getAdFromId(String UUID);
    void createAdvert(Date datePublished, String uniqueOwnerID, String title, List<String> imgURLs,
                      String description, int price, Advert.Condition condition, List<String> tags);

    void fetchAdvertsFromUserID(String userID, advertisementCallback advertisementCallback);
}

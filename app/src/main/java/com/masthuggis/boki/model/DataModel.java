package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.Repository;

import java.util.ArrayList;
import java.util.List;

public class DataModel {


    private static DataModel instance;
    private iUser user;
    private List<Advertisement> allAds = new ArrayList<>();
    private List<Advertisement> currentUserAds = new ArrayList<>();


    private DataModel() {
        fetchAllAdverts();

    }

    public static DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();

        }
        return instance;
    }

    public void addAdvertisement(Advertisement ad) {

        this.allAds.add(ad);
    }

    //Same functionality as above method but based off of firebase
    public Advertisement getAdFromAdID(String ID) {
        for (Advertisement ad : allAds) {
            if (ad.getUniqueID().equals(ID))
                return ad;
        }
        return null; //TODO Fix a better solution to handle NPExc....
    }

    //Returns a list of advertisements of the current user.
    public List<Advertisement> getAdsFromUniqueOwnerID(String ID) {
        List<Advertisement> userAds = new ArrayList<>();
        for (Advertisement ad : allAds) {
            if (ad.getUniqueOwnerID().equals(ID))
                userAds.add(ad);
        }
        return userAds;
    }

    private void fetchAllAdverts() {
        allAds.clear();
        Repository.fetchAllAdverts(advertisements -> {
            allAds = advertisements;


        });
    }

    public void updateAds() {
       fetchAllAdverts();
    }

    public List<Advertisement> getAllAds() {
        return new ArrayList<>(allAds);
    }
}

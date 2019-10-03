package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.RepositoryObserver;
import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.backend.advertisementCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataModel {


    private static DataModel instance;
    private iUser user;
    private List<Advertisement> allAds = new ArrayList<>();
    private List<Advertisement> currentUserAds = new ArrayList<>();


    private DataModel() {
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

    private void updateAllAds() {
        Repository.fetchAllAdverts(advertisements -> allAds = advertisements);
    }

    public void fetchAllAdverts(advertisementCallback advertisementCallback) {
        Repository.fetchAllAdverts(advertisementCallback);
        updateAllAds();
    }

    public void addRepositoryObserver(RepositoryObserver observer) {
        Repository.addRepositoryObserver(observer);
    }

    public List<Advertisement> getAllAds() {
        Repository.updateAdverts();
        return new ArrayList<>(allAds);
    }


    public void loggedIn(iUser user) {

        this.user = user;
    }

    public void loggedOut() {
        this.user = null;
    }

    public String getUserID() {
        return this.user.getId();
    }

    public String getUserEmail() {
        return this.getUserEmail();
    }

    public String getUserDisplayName() {
        return this.user.getDisplayName();
    }

    public boolean isLoggedIn() {

        return this.user != null;
    }

    public List<iChat> getUserChats() {

        return user.getChats();
    }

    public void init() {
        //simply here to be instantiated
    }

    public void createNewChat(String uniqueReceiverID) {
        HashMap<String, Object> newChatMap = new HashMap<>();
        newChatMap.put("receiver", uniqueReceiverID);
        newChatMap.put("sender", this.getUserID());
        UserRepository.getInstance().createNewChat(newChatMap);
    }

    public void sendMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        UserRepository.getInstance().writeMessage(uniqueChatID, messageMap);
    }

    public iChat findChatByID(String ID) {
        List<iChat> chats = user.getChats();
        for (iChat chat : chats) {
            if (chat.getChatID().equals(ID)) {
                return chat;
            }
        }
        return null;

    }

}

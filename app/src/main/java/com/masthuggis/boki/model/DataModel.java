package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.BackendDataHandler;
import com.masthuggis.boki.backend.FailureCallback;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.SuccessCallback;
import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.backend.advertisementCallback;
import com.masthuggis.boki.backend.messagesCallback;
import com.masthuggis.boki.backend.stringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataModel implements BackendObserver {


    private static DataModel instance;
    private iUser user;
    private List<Advertisement> allAds = new ArrayList<>();
    private List<Advertisement> currentUserAds = new ArrayList<>();

    private final List<ChatObserver> chatObservers = new ArrayList<>();
    private final List<AdvertisementObserver> advertisementObservers = new ArrayList<>();
    private final List<MessagesObserver> messagesObservers = new ArrayList<>();


    private DataModel() {
        BackendDataHandler.getInstance().addBackendObserver(this);
    }

    public static DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
            instance.initApp();
        }
        return instance;
    }

    private void initApp() {
        if (isLoggedIn()) {
            UserRepository.getInstance().logUserIn();
        }
    }

    public void addChatObserver(ChatObserver chatObserver) {
        this.chatObservers.add(chatObserver);
    }

    public void addMessagesObserver(MessagesObserver messagesObserver) {
        this.messagesObservers.add(messagesObserver);
    }

    public void addAdvertisementObserver(AdvertisementObserver advertisementObserver) {
        this.advertisementObservers.add(advertisementObserver);
    }

    public void removeChatObserver(ChatObserver chatObserver) {
        this.chatObservers.remove(chatObserver);
    }

    public void removeMessagesObserver(MessagesObserver messagesObserver) {
        this.messagesObservers.remove(messagesObserver);
    }

    public void removeAdvertisementObserver(AdvertisementObserver advertisementObserver) {
        this.advertisementObservers.remove(advertisementObserver);
    }


    private void notifyChatObservers() {
        for (ChatObserver chatObserver : chatObservers) {
            chatObserver.onChatUpdated();
        }
    }

    private void notifyMessagesObserver() {
        for (MessagesObserver messagesObserver : messagesObservers) {
            messagesObserver.onMessagesChanged();
        }
    }

    private void notifyAdvertisementObservers() {
        for (AdvertisementObserver advertisementObserver : advertisementObservers) {
            advertisementObserver.onAdvertisementsUpdated();
        }
    }


    public void SignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        UserRepository.getInstance().signIn(email, password, successCallback, failureCallback);
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

    public List<Advertisement> getAdsFromLoggedInUser() throws UserNotLoggedInException {
        if (isLoggedIn()) {
            return getAdsFromUniqueOwnerID(user.getId());
        } else {
            throw new UserNotLoggedInException();
        }
    }

    private void updateAllAds() {
        Repository.fetchAllAdverts(advertisements -> allAds = advertisements);
    }

    public void fetchAllAdverts(advertisementCallback advertisementCallback) {
        Repository.fetchAllAdverts(advertisements -> {
            allAds = advertisements;
            advertisementCallback.onCallback(allAds);
        });
    }

    public List<Advertisement> getAllAds() {
        return new ArrayList<>(allAds);
    }

    public void loggedIn(iUser user) {
        this.user = user;
    }

    public void loggedOut() {
        this.user = null;
    }

    public String getUserID() {
        return user.getId();
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    public String getUserDisplayName() {
        return this.user.getDisplayName();
    }

    public boolean isLoggedIn() {
        return UserRepository.getInstance().isUserLoggedIn();
    }

    public List<iChat> getUserChats() {

        return user.getChats();
    }


    public void createNewChat(Advertisement advertisement, stringCallback stringCallback) {

        HashMap<String, Object> newChatMap = new HashMap<>();
        //newChatMap.put("advertisement", advertisement);
        newChatMap.put("sender", this.getUserID());
/*
        newChatMap.put("receiver", uniqueReceiverID);
        newChatMap.put("sender", this.getUserID());

        newChatMap.put("receiverUsername", advertisement.getOwner());
        newChatMap.put("imgURL", imgURL);
        newChatMap.put("advertID", advertID);*/
        UserRepository.getInstance().createNewChat(newChatMap,advertisement, stringCallback);
    }

    public void sendMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        UserRepository.getInstance().writeMessage(uniqueChatID, messageMap);
    }

    public iChat findChatByID(String ID) {
        List<iChat> chats = user.getChats();
        if (!(chats == null)) {
            for (iChat chat : chats) {
                if (chat.getChatID().equals(ID)) {
                    return chat;
                }
            }
        }
        return null;
    }

    public void removeExistingAdvert(String uniqueID){
        BackendDataHandler.getInstance().deleteAd(uniqueID);
    }

    public void updateTitle(String adID, String newTitle){
        BackendDataHandler.getInstance().editTitle(adID, newTitle);
    }
    public void updatePrice(String adID, String newPrice ){
        BackendDataHandler.getInstance().editPrice(adID,newPrice);
    }

    public void updateDescription(String adID, String newDescription){
        BackendDataHandler.getInstance().editDescription(adID, newDescription);
    }

    public void updateImage(File imageFile, String adID){
        BackendDataHandler.getInstance().uploadImageToFirebase(imageFile, adID);
    }

   /* public void setUsername(String username) {
        UserRepository.getInstance().setUsername(username);
    }*/

    public void signInAfterRegistration(String email, String password, String username) {
        UserRepository.getInstance().signInAfterRegistration(email, password, username);
    }

    public void signOut() {
        UserRepository.getInstance().signOut();
    }

    public void signUp(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback) {
        UserRepository.getInstance().signUp(email, password, successCallback, failureCallback);
        signInAfterRegistration(email, password, username);
    }

    @Override
    public void onMessagesChanged() {
        notifyMessagesObserver();

    }

    @Override
    public void onAdvertisementsChanged() {
        notifyAdvertisementObservers();

    }

    @Override
    public void onChatsChanged() {
        notifyChatObservers();

    }

    public void getMessages(String uniqueChatID, Chat chat, messagesCallback messagesCallback) {
        UserRepository.getInstance().getMessages(uniqueChatID, chat, new messagesCallback() {
            @Override
            public void onCallback(List<iMessage> messagesList) {
                messagesCallback.onCallback(messagesList);
                notifyMessagesObserver();
            }
        });
    }


}
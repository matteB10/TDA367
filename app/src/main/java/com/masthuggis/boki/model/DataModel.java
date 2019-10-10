package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.BackendFactory;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.RepositoryFactory;
import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.backend.callbacks.chatCallback;
import com.masthuggis.boki.backend.callbacks.messagesCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.observers.AdvertisementObserver;
import com.masthuggis.boki.model.observers.BackendObserver;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.model.observers.MessagesObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataModel implements BackendObserver {

    private static DataModel instance;
    private iUser user;
    private List<Advertisement> allAds = new ArrayList<>();

    private final List<ChatObserver> chatObservers = new ArrayList<>();
    private final List<AdvertisementObserver> marketAdvertisementObservers = new ArrayList<>();
    private final List<AdvertisementObserver> userAdvertisementObservers = new ArrayList<>();
    private final List<MessagesObserver> messagesObservers = new ArrayList<>();
    private Repository repository;
    private UserRepository userRepository;


    private DataModel() {
        initBackend();
        initUser();
    }

    private void initBackend() {
        repository = RepositoryFactory.createRepository(BackendFactory.createBackend(),this);
        userRepository = RepositoryFactory.createUserRepository(BackendFactory.createBackend(),this);
        repository.addObserverToBackend(this);
    }

    public static DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
        }
        return instance;
    }

    private void initUser() {
        if (isLoggedIn()) {
            userRepository.logUserIn();
        }
    }

    public void addChatObserver(ChatObserver chatObserver) {
        this.chatObservers.add(chatObserver);
    }

    public void addMessagesObserver(MessagesObserver messagesObserver) {
        this.messagesObservers.add(messagesObserver);
    }

    public void addMarketAdvertisementObserver(AdvertisementObserver advertisementObserver) {
        this.marketAdvertisementObservers.add(advertisementObserver);
    }

    public void addUserAdvertisementObserver(AdvertisementObserver advertisementObserver) {
        this.userAdvertisementObservers.add(advertisementObserver);
    }

    public void removeChatObserver(ChatObserver chatObserver) {
        this.chatObservers.remove(chatObserver);
    }

    public void removeMessagesObserver(MessagesObserver messagesObserver) {
        this.messagesObservers.remove(messagesObserver);
    }

    public void removeAdvertisementObserver(AdvertisementObserver advertisementObserver) {
        this.marketAdvertisementObservers.remove(advertisementObserver);
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

    private void notifyMarketAdvertisementObservers() {
        for (AdvertisementObserver advertisementObserver : marketAdvertisementObservers) {
            advertisementObserver.onAdvertisementsUpdated();
        }
    }

    private void notifyUserAdvertisementObservers() {
        for (AdvertisementObserver advertisementObserver : userAdvertisementObservers) {
            advertisementObserver.onAdvertisementsUpdated();
        }
    }

    public void SignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        userRepository.signIn(email, password, successCallback, failureCallback);
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

    public void getAdsFromLoggedInUser(advertisementCallback advertisementCallback) {
        if (user == null) {
            return;
        }

        if (allAds == null || allAds.isEmpty()) {
            fetchAllAdverts(advertisements -> advertisementCallback.onCallback(retrieveAdsFromUserID(advertisements)));
        } else {
            advertisementCallback.onCallback(retrieveAdsFromUserID(allAds));
        }
    }

    private List<Advertisement> retrieveAdsFromUserID(List<Advertisement> adverts) {
        List<Advertisement> userAds = new ArrayList<>();
        for (Advertisement ad : adverts) {
            if (ad.getUniqueOwnerID().equals(user.getId()))
                userAds.add(ad);
        }
        return userAds;
    }

    private void updateAllAds() {
        repository.fetchAllAdverts(advertisements -> allAds = advertisements);
    }

    public void fetchAllAdverts(advertisementCallback advertisementCallback) {

        repository.fetchAllAdverts(new advertisementCallback() {
            @Override
            public void onCallback(List<Advertisement> advertisements) {
                allAds = advertisements;
                advertisementCallback.onCallback(allAds);
            }
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
        notifyChatObservers();
        notifyMessagesObserver();
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
        return userRepository.isUserLoggedIn();
    }

    public List<iChat> getUserChats() {

        return user.getChats();
    }


    public void createNewChat(Advertisement advertisement, stringCallback stringCallback) {

        HashMap<String, Object> newChatMap = new HashMap<>();
        newChatMap.put("senderID", this.getUserID());

        userRepository.createNewChat(newChatMap, advertisement, stringCallback);
    }

    public void sendMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        userRepository.writeMessage(uniqueChatID, messageMap);
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

    public void removeExistingAdvert(String uniqueID) {
        repository.deleteAd(uniqueID);
    }

    public void updateTitle(String adID, String newTitle) {
        repository.editTitle(adID, newTitle);
    }

    public void updatePrice(String adID, String newPrice) {
        repository.editPrice(adID, newPrice);
    }

    public void updateDescription(String adID, String newDescription) {
        repository.editDescription(adID, newDescription);
    }

    public void updateImage(File imageFile, String adID){
        repository.uploadImageToFirebase(imageFile, adID);
    }

   /* public void setUsername(String username) {
        userRepository.setUsername(username);
    }*/

    public void signInAfterRegistration(String email, String password, String username) {
        userRepository.signInAfterRegistration(email, password, username);
    }

    public void signOut() {
        userRepository.signOut();
    }

    public void signUp(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback) {
        userRepository.signUp(email, password, new SuccessCallback() {
            @Override
            public void onSuccess() {
                signInAfterRegistration(email, password, username);
                successCallback.onSuccess();

            }
        }, failureCallback);
    }

    @Override
    public void onMessagesChanged() {
        notifyMessagesObserver();
    }

    @Override
    public void onAdvertisementsChanged() {
        notifyMarketAdvertisementObservers();
    }

    @Override
    public void onChatsChanged() {
        notifyChatObservers();
    }

    void getMessages(String uniqueChatID, Chat chat, messagesCallback messagesCallback) {
        userRepository.getMessages(uniqueChatID, chat, new messagesCallback() {
            @Override
            public void onCallback(List<iMessage> messagesList) {
                messagesCallback.onCallback(messagesList);
                notifyMessagesObserver();
            }
        });
    }

    public void addToFavourites(String adID) {
        String userID = getUserID();
        repository.addToFavourites(adID, userID);
    }

    public void saveAdvert(File currentImageFile, Advertisement advertisement) {
        repository.saveAdvert(currentImageFile, advertisement);
    }

    void fetchUserChats(String userID, chatCallback chatCallback) {
        userRepository.getUserChats(userID, new chatCallback() {
            @Override
            public void onCallback(List<iChat> chatsList) {
                chatCallback.onCallback(chatsList);
            }
        },this);
    }
}
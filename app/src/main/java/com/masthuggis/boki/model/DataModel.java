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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataModel implements BackendObserver {


    private static DataModel instance;
    private iUser user;
    private List<Advertisement> allAds = new ArrayList<>();

    private final List<ChatObserver> chatObservers = new ArrayList<>();
    private final List<AdvertisementObserver> advertisementObservers = new ArrayList<>();
    private final List<MessagesObserver> messagesObservers = new ArrayList<>();
    private Repository repository;
    private UserRepository userRepository;


    private DataModel() {

        initBackend();
    }

    private void initBackend() {
        repository = RepositoryFactory.createRepository(BackendFactory.createBackend());
        userRepository = RepositoryFactory.createUserRepository(BackendFactory.createBackend());
        repository.addObserverToBackend(this);
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
            userRepository.logUserIn();
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
        repository.fetchAllAdverts(advertisements -> allAds = advertisements);
    }

    public void fetchAllAdverts(advertisementCallback advertisementCallback) {

        repository.fetchAllAdverts(new advertisementCallback() {
            @Override
            public void onCallback(List<Advertisement> advertisements) {
                allAds = advertisements;
                advertisementCallback.onCallback(allAds);
                notifyAdvertisementObservers();
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
        newChatMap.put("sender", this.getUserID());

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
        userRepository.signUp(email, password, successCallback, failureCallback);
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

    void getMessages(String uniqueChatID, Chat chat, messagesCallback messagesCallback) {
        userRepository.getMessages(uniqueChatID, chat, new messagesCallback() {
            @Override
            public void onCallback(List<iMessage> messagesList) {
                messagesCallback.onCallback(messagesList);
                notifyMessagesObserver();
            }
        });
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
        });
    }
}
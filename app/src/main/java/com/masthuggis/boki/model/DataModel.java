package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.BackendFactory;
import com.masthuggis.boki.model.callbacks.FailureCallback;
import com.masthuggis.boki.model.callbacks.MarkedAsFavouriteCallback;
import com.masthuggis.boki.model.callbacks.SuccessCallback;
import com.masthuggis.boki.model.callbacks.advertisementCallback;
import com.masthuggis.boki.model.callbacks.chatCallback;
import com.masthuggis.boki.model.callbacks.messagesCallback;
import com.masthuggis.boki.model.callbacks.stringCallback;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.model.observers.MessagesObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Used by ChatPresenter,CreateAdPresenter, DependencyInjector, DetailsPresenter
 * FavouritesPresenter,HomePresenter,ListPresenter, MainPresenter,
 * MessagesPresenter,ProfilePresenter, SignUpPresenter, SignInPresenter
 * Written by masthuggis
 */
public class DataModel {

    private static DataModel instance;
    private iUser user;
    private List<Advertisement> allAds = new ArrayList<>();


    private iRepository repository;

    private DataModel() {
        initBackend();
    }

    public static DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
        }
        return instance;
    }

    private void initBackend() {
        repository = RepositoryFactory.createRepository(BackendFactory.createBackend());
        attachAdvertsObserver();
    }


    /**
     * Initializes all fields in the User-object of the application with data gotten from firebase with the corresponding userID
     * Is a must to call each init-related method in the callback of the previous one to make sure that all methods are
     * completed, since a method called first does not always finish first.
     */
    public void initUser(SuccessCallback successCallback) {
        if (user != null) {
            successCallback.onSuccess();
            return;
        }

        repository.initialAdvertFetch(allAdvertInMarket -> {
            allAds = allAdvertInMarket;
            repository.getUser(newUser -> {
                user = newUser;
                user.setAdverts(getAdsFromCurrentUser());
                getFavouritesFromLoggedInUser(advertisements -> {
                    user.setFavourites(advertisements);
                    fetchUserChats(user.getId(), chatsList -> {
                        user.setChats(chatsList);
                        initMessages();
                        successCallback.onSuccess();
                        if(!repository.marketListenerSet()){
                            attachAdvertsObserver();
                        }
                    });
                });
            });
        });
    }

    private void initMessages() {
        for (iChat chat : user.getChats()) {
            getMessages(chat.getChatID(), chat::setMessages);
        }
    }

    private void getMessages(String uniqueChatID, messagesCallback messagesCallback) {
        repository.getMessages(uniqueChatID, messagesCallback);
    }


    public void signIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        repository.signIn(email, password, successCallback, failureCallback);
    }

    public Advertisement getAdFromAdID(String ID) {
        for (Advertisement ad : allAds) {
            if (ad.getUniqueID().equals(ID))
                return ad;

        }
        return null;
    }

    public List<Advertisement> getAdsFromCurrentUser() {
        List<Advertisement> userAds = new ArrayList<>();
        for (Advertisement ad : allAds) {
            if (ad.getUniqueOwnerID().equals(user.getId())) {
                userAds.add(ad);
            }
        }
        return userAds;
    }

    /**
     * Returns a list containing all advertisements the current user has as favourites in the backend,
     * does so via the parameter-callback
     */
    private void getFavouritesFromLoggedInUser(advertisementCallback advertisementCallback) {
        if (allAds.size() == 0) {
            advertisementCallback.onCallback(new ArrayList<>());
        }
        List<Advertisement> favourites = new ArrayList<>();
        int i = allAds.size() - 1;
        for (Advertisement ad : allAds) {
            isAFavouriteInDatabase(ad.getUniqueID(), markedAsFavourite -> {
                if (markedAsFavourite) {
                    favourites.add(ad);
                }
            });
            if (allAds.get(i).equals(ad)) {
                advertisementCallback.onCallback(favourites);
            }
        }
    }


    /**
     * Returns a boolean of whether or not the given adID exists under the User's list of ID:s in the database
     */
    private void isAFavouriteInDatabase(String uniqueAdID, MarkedAsFavouriteCallback markedAsFavouriteCallback) {
        repository.getUserFavourites(user.getId(), favouriteIDs -> {
            if (favouriteIDs != null) { //Only check if user actually has favourites, otherwise NullPointerException
                for (String favouriteID : favouriteIDs) {
                    if (adStillExists(favouriteID) && favouriteID.equals(uniqueAdID)) {
                        markedAsFavouriteCallback.onCallback(true); //Only call method if true, would otherwise call when not needed
                    }
                }
            }
        });
    }

    /**
     * Iterates over the local list of favourites belonging to the current user on the device.
     *
     * @return a boolean of whether or not there exists an advertisement with the same ID as the paramater-advertisement.
     */

    public boolean isAFavourite(Advertisement advertisement) {
        List<Advertisement> userFavourites = user.getFavourites();
        for (Advertisement favourite : userFavourites) {
            if (favourite.getUniqueID().equals(advertisement.getUniqueID())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an ad still exists or not. If it doesn't the chat is removed from the current
     * users list of favourites.
     */
    private boolean adStillExists(String favouriteID) {
        for (Advertisement advertisement : allAds) {
            if (advertisement.getUniqueID().equals(favouriteID)) {
                return true;
            }
        }
        deleteIDFromFavourites(user.getId(), favouriteID);
        return false;
    }

    private void deleteIDFromFavourites(String id, String favouriteID) {
        repository.deleteIDFromFavourites(id, favouriteID);
    }

    private void attachAdvertsObserver() {
        repository.attachAdvertsObserver(advertisements -> {
            allAds = advertisements;
        });
    }

    public List<Advertisement> getAllAdverts() {
        return allAds;
    }

    public boolean isUserOwner(Advertisement advertisement) {
        return user.getId().equals(advertisement.getUniqueOwnerID());
    }

    private void loggedOut() {
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
        return repository.isUserLoggedIn();
    }

    public List<iChat> getUserChats() {
        if (user != null) {
            return user.getChats();
        }
        return new ArrayList<>();
    }

    public List<Advertisement> getUserFavourites() {
        return user.getFavourites();
    }


    public void createNewChat(String adOwnerID, String adBuyerID, String
            advertID, String imageURL, stringCallback stringCallback) {
        repository.createNewChat(adOwnerID, adBuyerID, advertID, imageURL, stringCallback);
    }

    public void sendMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        repository.writeMessage(uniqueChatID, messageMap);
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

    /**
     * Removes an advertisement from the database.
     * Puts the necessary information into maps to send to the backend to
     * make sure the advertisement is both removed from the database
     * and notifies all users currently engaged in conversations
     * about the removal of the discussed advertisement.
     * @param adID Id of the advertisement.
     * @param userID ID of the owner of the advertisement.
     */
    public void removeExistingAdvert(String adID, String userID) {
        List<Map<String, String>> chatReceiverAndUserIDMap = new ArrayList<>();
        Map<String, String> adIDAndUserID = new HashMap<>();
        adIDAndUserID.put("adID", adID);
        adIDAndUserID.put("userID", userID);
        for (iChat chat : user.getChats()) {
            Map<String, String> map = new HashMap<>();
            if (chat.getAdID().equals(adID)) {
                map.put("receiverID", chat.getReceiverID(user.getId()));
                map.put("chatID", chat.getChatID());
                chatReceiverAndUserIDMap.add(map);
            }
        }
        repository.deleteAd(chatReceiverAndUserIDMap, adIDAndUserID);
    }


    public void updateAd(File imageFile, Advertisement ad) {
        repository.updateAdvert(imageFile, ad);
    }

    public void saveAdvert(File currentImageFile, Advertisement advertisement) {
        repository.saveAdvert(currentImageFile, advertisement);
    }


    public void signOut() {
        repository.signOut();
        loggedOut();
    }

    public void signUp(String email, String password, String username, SuccessCallback
            successCallback, FailureCallback failureCallback) {
        repository.signUp(email, password, username, successCallback, failureCallback);
    }

    /**
     * Adds an advertisement to the user's favourites
     * Adds the advert in firebase via user and advert ID
     * Removes it from the local list via the actual advertisement-object
     *
     * @param advertisement
     */
    public void addToFavourites(Advertisement advertisement) {
        String userID = getUserID();
        repository.addToFavourites(advertisement.getUniqueID(), userID);
        user.addFavourite(advertisement);
    }

    /**
     * Removes a given advertisement from user's favourites
     * Removes in firebase via user and advert ID
     * Removes in local list via the actual advertisement-object
     *
     * @param advertisement the advertisement to be removed
     */
    public void removeFromFavourites(Advertisement advertisement) {
        String userID = getUserID();
        repository.removeFromFavourites(advertisement.getUniqueID(), userID);
        user.removeFavourite(advertisement);
    }

    private void fetchUserChats(String userID, chatCallback chatCallback) {
        repository.getUserChats(userID, chatCallback);
    }

    public void removeChat(String userID, String chatID) {
        repository.removeChat(userID, chatID);
    }

    public String findChatID(String adID) {
        if (getUserChats() != null) {
            for (iChat chat : getUserChats()) {
                if (chat.getAdID().equals(adID)) {
                    return chat.getChatID();
                }
            }
        }
        return null;
    }

    public void addChatObserver(ChatObserver chatObserver) {
        repository.addChatObserver(chatObserver);
    }

    public void removeChatObserver(ChatObserver chatObserver) {
        repository.removeChatObserver(chatObserver);
    }

    public void addMessagesObserver(MessagesObserver messagesObserver) {
        repository.addMessagesObserver(messagesObserver);
    }

    public void removeMessagesObserver(MessagesObserver messagesObserver) {
        repository.removeMessagesObserver(messagesObserver);
    }
}
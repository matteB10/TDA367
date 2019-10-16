package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.BackendFactory;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.FavouriteIDsCallback;
import com.masthuggis.boki.backend.callbacks.MarkedAsFavouriteCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.backend.callbacks.chatCallback;
import com.masthuggis.boki.backend.callbacks.messagesCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.backend.callbacks.userCallback;
import com.masthuggis.boki.backend.iRepository;
import com.masthuggis.boki.model.observers.AdvertisementObserver;
import com.masthuggis.boki.model.observers.BackendObserver;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.model.observers.MessagesObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataModel implements BackendObserver {

    private static DataModel instance;
    private iUser user;
    private List<Advertisement> allAds = new ArrayList<>();

    private final List<ChatObserver> chatObservers = new ArrayList<>();
    private final List<AdvertisementObserver> marketAdvertisementObservers = new ArrayList<>();
    private final List<AdvertisementObserver> userAdvertisementObservers = new ArrayList<>();
    private final List<MessagesObserver> messagesObservers = new ArrayList<>();
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
        repository = new Repository(BackendFactory.createBackend());
        repository.addBackendObserver(this);
        attachAdvertsObserver();
    }


    /**
     * Initializes all fields in the User-object of the application with data gotten from firebase with the corresponding userID
     */
    public void initUser(SuccessCallback successCallback) {
        initialAdvertFetch(new SuccessCallback() {
            @Override
            public void onSuccess() {
                repository.getUser(new userCallback() {
                    @Override
                    public void onCallback(iUser newUser) {
                        user = newUser;
                        user.setAdverts(getAdsFromCurrentUser());
                        fetchUserChats(user.getId(), new chatCallback() {
                            @Override
                            public void onCallback(List<iChat> chatsList) {
                                user.setChats(chatsList);
                                initMessages();
                                getFavouritesFromLoggedInUser(advertisements -> {
                                    user.setFavourites(advertisements);
                                    successCallback.onSuccess();
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void initMessages() {
        for (iChat chat : user.getChats()) {
            getMessages(chat.getChatID(), new messagesCallback() {
                @Override
                public void onCallback(List<iMessage> messagesList) {
                    chat.setMessages(messagesList);
                }
            });
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

    public void signIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        repository.signIn(email, password, new SuccessCallback() {
            @Override
            public void onSuccess() {
                successCallback.onSuccess();
            }
        }, failureCallback);
    }

    public void addAdvertisement(Advertisement ad) {
        this.allAds.add(ad);
    }

    public Advertisement getAdFromAdID(String ID) {
        for (Advertisement ad : allAds) {
            if (ad.getUniqueID().equals(ID))
                return ad;
        }
        return null; //TODO Fix a better solution to handle NPExc....
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
     * @return A list containing all advertisements the current user has as favourites in the backend
     */
    private void getFavouritesFromLoggedInUser(advertisementCallback advertisementCallback) {
        if (allAds.size() == 0) {
            advertisementCallback.onCallback(new ArrayList<>());
        }
        List<Advertisement> favourites = new ArrayList<>();
        int i = allAds.size() - 1;
        for (Advertisement ad : allAds) {
            isAFavouriteInDatabase(ad.getUniqueID(), new MarkedAsFavouriteCallback() {
                @Override
                public void onCallback(boolean markedAsFavourite) {
                    if (markedAsFavourite) {
                        ad.markAsFavourite();
                        favourites.add(ad);
                    }
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
        repository.getUserFavourites(new FavouriteIDsCallback() {
            @Override
            public void onCallback(List<String> favouriteIDs) {
                if (favouriteIDs != null) { //Only check if user actually has favourites, otherwise NullPointerException
                    for (String favouriteID : favouriteIDs) {
                        if (adStillExists(favouriteID) && favouriteID.equals(uniqueAdID)) {
                            markedAsFavouriteCallback.onCallback(true); //Only call method if true, would otherwise call when not needed
                        }
                    }
                }
            }
        });
    }

    public boolean isAFavourite(Advertisement advertisement) {
        List<Advertisement> userFavourites = user.getFavourites();
        for (Advertisement favourite : userFavourites) {
            if (favourite.getUniqueID().equals(advertisement.getUniqueID())) {
                return true;
            }
        }
        return false;
    }


    private boolean adStillExists(String favouriteID) {
        for (Advertisement advertisement : allAds) {
            if (advertisement.getUniqueID().equals(favouriteID)) {
                return true;
            }
        }
        //If this is reached, no existing advertisement has the ID that is stored under user favourites in database
        deleteIDFromFavourites(favouriteID);
        return false;
    }

    private void deleteIDFromFavourites(String favouriteID) {
        repository.deleteIDFromFavourites(favouriteID);
    }


    private List<Advertisement> retrieveAdsFromUserID(List<Advertisement> adverts) {
        List<Advertisement> userAds = new ArrayList<>();
        for (Advertisement ad : adverts) {
            if (ad.getUniqueOwnerID().equals(user.getId()))
                userAds.add(ad);
        }
        user.setAdverts(userAds);
        return userAds;
    }


    void initialAdvertFetch(SuccessCallback successCallback) {
        repository.initialAdvertFetch(new advertisementCallback() {
            @Override
            public void onCallback(List<Advertisement> advertisements) {
                allAds = advertisements;
                successCallback.onSuccess();
            }
        });
    }

    void attachAdvertsObserver() {
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

    public void loggedOut() {
        this.user = null;
    }

    public String getUserID() {
        return user.getId();
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    String getUserDisplayName() {
        return this.user.getDisplayName();
    }

    public boolean isLoggedIn() {
        return repository.isUserLoggedIn();
    }

    public List<iChat> getUserChats() {
        return user.getChats();
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


    public void removeExistingAdvert(String adID, String userID) {
        List<Map<String, String>> chatReceiverAndUserIDMap = new ArrayList<>();
        Map<String, String> adIDAndUserID = new HashMap<>();
        adIDAndUserID.put("adID", adID);
        adIDAndUserID.put("userID", userID);
        List<String> chatIDs = new ArrayList<>();
        for (iChat chat : user.getChats()) {
            Map<String, String> map = new HashMap<>();
            if (chat.getAdID().equals(adID)) {

                map.put("receiverID", chat.getReceiverID(user.getId()));
                map.put("chatID", chat.getChatID());
                chatReceiverAndUserIDMap.add(map);
            }
        }
        user.getChatIDFromAdID(adID);
        repository.deleteAd(chatReceiverAndUserIDMap, adIDAndUserID);
    }


    public void updateAd(File imageFile, Advertisement ad) {
        repository.updateAdvert(imageFile, ad);
    }

    public void saveAdvert(File currentImageFile, Advertisement advertisement) {
        repository.saveAdvert(currentImageFile, advertisement);
    }

   /* public void setUsername(String username) {
        userRepository.setUsername(username);
    }*/


    public void signOut() {
        repository.signOut();
    }

    public void signUp(String email, String password, String username, SuccessCallback
            successCallback, FailureCallback failureCallback) {
        repository.signUp(email, password, username, new SuccessCallback() {
            @Override
            public void onSuccess() {
                successCallback.onSuccess();
           /*     initUser(new SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        successCallback.onSuccess();

                    }
                });*/
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

    private void getMessages(String uniqueChatID, messagesCallback messagesCallback) {
        repository.getMessages(uniqueChatID, new messagesCallback() {
            @Override
            public void onCallback(List<iMessage> messagesList) {
                messagesCallback.onCallback(messagesList);
                notifyMessagesObserver();
            }
        });
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
        repository.getUserChats(userID, new chatCallback() {
            @Override
            public void onCallback(List<iChat> chatsList) {
                chatCallback.onCallback(chatsList);
            }
        });
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

}
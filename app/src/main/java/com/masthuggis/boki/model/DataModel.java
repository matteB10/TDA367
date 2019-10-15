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
        // initUser();
        fetchAllAdverts();
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
    }

    public void initUser(SuccessCallback successCallback) {
        if (isLoggedIn()) {
            repository.getUser(new userCallback() {
                @Override
                public void onCallback(iUser newUser) {
                    user = newUser;
                    fetchUserChats(user.getId(), new chatCallback() {
                        @Override
                        public void onCallback(List<iChat> chatsList) {
                            user.setChats(chatsList);
                            user.setAdverts(getAdsFromCurrentUser());
                            initMessages();
                            successCallback.onSuccess();
                            user.setFavourites(getFavouritesFromLoggedInUser());
                            successCallback.onSuccess();
                            //TODO fix this maybe
                        }
                    });
                }
            });
        }
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
                initUser(new SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        successCallback.onSuccess();
                    }
                });
            }
        }, failureCallback);
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

    public List<Advertisement> getAdsFromCurrentUser() {
        List<Advertisement> userAds = new ArrayList<>();
        for (Advertisement ad : allAds) {
            if (ad.getUniqueOwnerID().equals(user.getId())) {
                userAds.add(ad);
            }
        }
        return userAds;
    }


    private List<Advertisement> getFavouritesFromLoggedInUser() {
        List<Advertisement> favourites = new ArrayList<>();
        for (Advertisement ad : allAds) {
            isAdMarkedAsFavourite(ad.getUniqueID(), new MarkedAsFavouriteCallback() {
                @Override
                public void onCallback(boolean markedAsFavourite) {
                    favourites.add(ad);
                }
            });
        }
        return favourites;
    }

    //Yikes
    public void isAdMarkedAsFavourite(String uniqueAdID, MarkedAsFavouriteCallback markedAsFavouriteCallback) {
        repository.getUserFavourites(new FavouriteIDsCallback() {
            @Override
            public void onCallback(List<String> favouriteIDs) {
                if (favouriteIDs != null) { //Only check if user actually has favourites, otherwise NullPointerException
                    for (String adID : favouriteIDs) {
                        if (adID.equals(uniqueAdID))
                            markedAsFavouriteCallback.onCallback(true);
                    }
                }
            }
        });
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


    void fetchAllAdverts() {
        repository.fetchAllAdverts(new advertisementCallback() {
            @Override
            public void onCallback(List<Advertisement> advertisements) {
                allAds = advertisements;
            }
        });
    }

    public List<Advertisement> getAllAdverts() {
        return allAds;
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


    public void updateAd(Advertisement ad, File imageFile) {
        String adID = ad.getUniqueID();
        String title = ad.getTitle();
        Long price = ad.getPrice();
        String description = ad.getDescription();
        List<String> tagList = ad.getTags();
        String condition = ad.getCondition().toString();
        repository.updateAd(adID, title, price, description, tagList, condition, imageFile);
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
                initUser(new SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        successCallback.onSuccess();

                    }
                });
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

    public void addToFavourites(String adID) {
        String userID = getUserID();
        repository.addToFavourites(adID, userID);
    }

    public void removeFromFavourites(String adID) {
        String userID = getUserID();
        repository.removeFromFavourites(adID, userID);
    }

    public void saveAdvert(File currentImageFile, Advertisement advertisement) {
        repository.saveAdvert(currentImageFile, advertisement);
    }

    void fetchUserChats(String userID, chatCallback chatCallback) {
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
}
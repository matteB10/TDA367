package com.masthuggis.boki.model;

import com.masthuggis.boki.model.callbacks.FailureCallback;
import com.masthuggis.boki.model.callbacks.FavouriteIDsCallback;
import com.masthuggis.boki.model.callbacks.SuccessCallback;
import com.masthuggis.boki.model.callbacks.advertisementCallback;
import com.masthuggis.boki.model.callbacks.chatCallback;
import com.masthuggis.boki.model.callbacks.messagesCallback;
import com.masthuggis.boki.model.callbacks.stringCallback;
import com.masthuggis.boki.model.callbacks.userCallback;
import com.masthuggis.boki.backend.iBackend;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.model.observers.MessagesObserver;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class providing the functionality to convert data from backend into objects to be used
 * by the domain-layer of the application.
 * Data is fetched through the iBackend interface.
 * This class simply delegates work to a proper repository.
 * Currently those are AdvertsRepository and UserRepository.
 * Used by RepositoryFactory
 * Written by masthuggis
 */
class Repository implements iRepository {

    private AdvertRepository advertRepository;
    private UserRepository userRepository;


    Repository(iBackend backend) {
        this.advertRepository = RepositoryFactory.createAdvertRepository(backend);
        this.userRepository = RepositoryFactory.createUserRepository(backend);
    }

    public void saveAdvert(File imageFile, Advertisement ad) {
        advertRepository.saveAdvert(imageFile, ad);
    }

    public void initialAdvertFetch(advertisementCallback advertisementCallback) {
        advertRepository.initialAdvertFetch(advertisementCallback);
    }

    public void attachAdvertsObserver(advertisementCallback advertisementCallback) {
        advertRepository.attachMarketListener(advertisementCallback);
    }

    public void deleteAd(List<Map<String, String>> chatReceiverAndUserIDMap, Map<String, String> adIDAndUserID) {
        advertRepository.deleteAd(chatReceiverAndUserIDMap, adIDAndUserID);
    }


    public void addToFavourites(String adID, String userID) {
        advertRepository.addToFavourites(adID, userID);
    }

    public void signIn(String email, String password, SuccessCallback successCallback,
                       FailureCallback failureCallback) {
        userRepository.signIn(email, password, successCallback, failureCallback);
    }

    public void signUp(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback) {
        userRepository.signUp(email, password, username, successCallback, failureCallback);
    }

    public boolean isUserLoggedIn() {
        return userRepository.isUserLoggedIn();
    }

    public void getUserChats(String userID, chatCallback chatCallback) {
        userRepository.getUserChats(userID, chatCallback);
    }

    public void removeFromFavourites(String adID, String userID) {
        advertRepository.removeAdFromFavourites(adID, userID);
    }

    @Override
    public void deleteIDFromFavourites(String id, String favouriteID) {
        userRepository.deleteIDFromFavourites(id, favouriteID);


    }

    @Override
    public void updateAdvert(File imageFile, Advertisement ad) {
        advertRepository.updateAdvert(imageFile, ad);

    }

    @Override
    public void addChatObserver(ChatObserver chatObserver) {
        userRepository.addChatObserver(chatObserver);
    }

    @Override
    public void removeChatObserver(ChatObserver chatObserver) {
        userRepository.removeChatObserver(chatObserver);
    }

    @Override
    public void removeMessagesObserver(MessagesObserver messagesObserver) {
        userRepository.removeMessagesObserver(messagesObserver);
    }

    @Override
    public void addMessagesObserver(MessagesObserver messagesObserver) {
        userRepository.addMessagesObserver(messagesObserver);

    }

    @Override
    public boolean marketListenerSet() {
        return advertRepository.marketListenerSet();
    }


    public void getMessages(String uniqueChatID, messagesCallback messagesCallback) {
        userRepository.getMessages(uniqueChatID, messagesCallback);
    }

    public void createNewChat(String adOwnerID, String adBuyerID, String advertID,String adOwnerUsername,String adBuyerUsername, String imageURL, stringCallback stringCallback) {
        userRepository.createNewChat(adOwnerID, adBuyerID, advertID,adOwnerUsername,adBuyerUsername, imageURL, stringCallback);
    }

    public void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        userRepository.writeMessage(uniqueChatID, messageMap);
    }

    public void signOut() {
        userRepository.signOut();

    }

    @Override
    public void getUser(userCallback userCallback) {
        userRepository.getCurrentUser(userCallback);
    }

    @Override
    public void removeChat(String userID, String chatID) {
        userRepository.removeChat(userID, chatID);

    }

    @Override
    public void getUserFavourites(String userID, FavouriteIDsCallback favouriteIDsCallback) {
        advertRepository.getUserFavourites(userID, favouriteIDsCallback);
    }

}

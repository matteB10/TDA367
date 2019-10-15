package com.masthuggis.boki.backend;

import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.FavouriteIDsCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.backend.callbacks.chatCallback;
import com.masthuggis.boki.backend.callbacks.messagesCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.backend.callbacks.userCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.observers.BackendObserver;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class providing the functionality to convert data from backend into objects to be used
 * by the domain-layer of the application.
 * Data is fetched through the iBackend interface.
 */
public class Repository implements iRepository {

    private AdvertRepository advertRepository;
    private UserRepository userRepository;


    public Repository(iBackend backend) {
        this.advertRepository = new AdvertRepository(backend);
        this.userRepository = new UserRepository(backend);
    }

    public void saveAdvert(File imageFile, Advertisement ad) {
        advertRepository.saveAdvert(imageFile, ad);
    }

    public void fetchAllAdverts(advertisementCallback advertisementCallback) {
        advertRepository.fetchAllAdverts(advertisementCallback);
    }

    public void deleteAd(List<Map<String, String>> chatReceiverAndUserIDMap, Map<String, String> adIDAndUserID){
        advertRepository.deleteAd(chatReceiverAndUserIDMap,adIDAndUserID);
    }

    public void updateAd(String adID, String newTitle, long newPrice, String newDescription,
                         List<String> newTagList, String newCondition, File imageFile) {
        advertRepository.updateAd(adID, newTitle, newPrice, newDescription, newTagList, newCondition, imageFile);
    }

    public void addBackendObserver(BackendObserver backendObserver) {
        advertRepository.addBackendObserver(backendObserver);
    }

    public void removeBackendObserver(BackendObserver backendObserver) {
        advertRepository.removeBackendObserver(backendObserver);
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


    public void getMessages(String uniqueChatID, messagesCallback messagesCallback) {
        userRepository.getMessages(uniqueChatID, messagesCallback);
    }

    public void createNewChat(String adOwnerID, String adBuyerID, String advertID,String imageURL, stringCallback stringCallback) {
        userRepository.createNewChat(adOwnerID, adBuyerID, advertID,imageURL, stringCallback);
    }

    public void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        userRepository.writeMessage(uniqueChatID, messageMap);
    }

    public void signOut() {
        userRepository.signOut();
    }

    @Override
    public void getUser(userCallback userCallback) {
        userRepository.getUser(userCallback);
    }

    @Override
    public void removeChat(String userID, String chatID) {
        userRepository.removeChat(userID,chatID);

    }


    @Override
    public void getUserFavourites(FavouriteIDsCallback favouriteIDsCallback) {
        advertRepository.getUserFavourites(favouriteIDsCallback);

    }

}

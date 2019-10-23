package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.callbacks.DBCallback;
import com.masthuggis.boki.model.callbacks.DBMapCallback;
import com.masthuggis.boki.model.callbacks.FailureCallback;
import com.masthuggis.boki.model.callbacks.SuccessCallback;
import com.masthuggis.boki.model.callbacks.stringCallback;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.model.observers.MessagesObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Class which delegates work to either BackendReader or BackendWriter depending on which method call.
 * Used by DataModel
 *
 */
public class BackendDataHandler implements iBackend {

    /**
     * Responsible for delegating methods to either BackendReader or BackendWriter depending
     * on which method is called.
     * Contains a List of chatObservers and a List of messagesObservers. It injects chatObservers and messages
     * Observers to BackendReader and chatObservers to BackendWriter.
     * This is done to make sure the model keeps up to date with the newest
     * data. See documentation considering specific methods in respective class.
     * Written by masthuggis
     */

    private final List<ChatObserver> chatObservers = new ArrayList<>();
    private final List<MessagesObserver> messagesObservers = new ArrayList<>();
    private final BackendReader backendReader = BackendFactory.createBackendReader(chatObservers,messagesObservers);
    private final BackendWriter backendWriter = BackendFactory.createBackendWriter(chatObservers);

    public void addChatObserver(ChatObserver chatObserver) {
        this.chatObservers.add(chatObserver);
    }

    public void addMessagesObserver(MessagesObserver messagesObserver) {
        this.messagesObservers.add(messagesObserver);
    }

    public void removeChatObserver(ChatObserver chatObserver) {
        this.chatObservers.remove(chatObserver);
    }

    public void removeMessagesObserver(MessagesObserver messagesObserver) {
        this.messagesObservers.remove(messagesObserver);
    }

    public void writeAdvertToFirebase(File imageFile, Map<String, Object> data) {
        backendWriter.writeAdvertToFirebase(imageFile, data);
    }

    public void getFavouriteIDs(String userID,DBMapCallback dbMapCallback) {
        backendReader.fetchFavouriteIDS(userID,dbMapCallback);
    }

    public void deleteIDFromFavourites(String id, String favouriteID) {
        backendWriter.deleteIDFromFavourites(id, favouriteID);
    }


    @Override
    public void initialAdvertFetch(DBCallback dbCallback) {
        backendReader.initialAdvertFetch(dbCallback);
    }


    public void attachMarketListener(DBCallback DBCallback) {
        backendReader.attachMarketListener(DBCallback);
    }


    public void getUserChats(String userID, DBCallback DBCallback, FailureCallback failureCallback) {
        backendReader.attachChatSnapshotListener(userID, DBCallback, failureCallback);
    }


    public void createNewChat(String adOwnerID, String otherUserID, String advertID, String imageURL, stringCallback stringCallback) {
        backendWriter.createNewChat(adOwnerID, otherUserID, advertID, imageURL, stringCallback);
    }

    @Override
    public void addAdToFavourites(String adID, String userID) {
        backendWriter.addAdToFavourites(adID, userID);
    }

    @Override
    public void removeAdFromFavourites(String adID, String userID) {
        backendWriter.removeAdFromFavourites(adID, userID);
    }

    @Override
    public void userSignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        backendWriter.userSignIn(email, password, successCallback, failureCallback);
    }

    public boolean isUserSignedIn() {
        return backendReader.isUserSignedIn();
    }

    public void userSignUpAndSignIn(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback) {
        backendWriter.userSignUpAndSignIn(email, password, username, successCallback, failureCallback);

    }

    @Override
    public void getUserFromID(String userID, DBMapCallback dbMapCallback) {
        backendReader.fetchUserFromID(userID, dbMapCallback);
    }

    public void getUser(DBMapCallback dbMapCallback) {
        backendReader.fetchCurrentUser(dbMapCallback);
    }

    public void writeMessage(String uniqueChatID, Map<String, Object> messageMap) {
        backendWriter.writeMessage(uniqueChatID, messageMap);
    }


    public void getMessages(String uniqueChatID, DBCallback messageCallback) {
        backendReader.attachMessagesSnapshotListener(uniqueChatID, messageCallback);
    }

    public void signOut() {
        backendWriter.signOut();
    }

    public void deleteAd(List<Map<String, String>> chatReceiverAndUserIDMap, Map<String, String> adIDAndUserID) {
        backendWriter.deleteAd(chatReceiverAndUserIDMap, adIDAndUserID);

    }
    @Override
    public void removeChat(String userID, String chatID) {
        backendWriter.removeChat(userID, chatID);
    }
    @Override
    public void updateAdToFirebase(File imageFile, Map<String, Object> dataMap) {
        backendWriter.updateAdToFirebase(imageFile, dataMap);
    }

}
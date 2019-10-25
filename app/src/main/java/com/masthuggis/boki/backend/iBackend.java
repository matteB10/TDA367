package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.callbacks.DBCallback;
import com.masthuggis.boki.model.callbacks.DBMapCallback;
import com.masthuggis.boki.model.callbacks.FailureCallback;
import com.masthuggis.boki.model.callbacks.SuccessCallback;
import com.masthuggis.boki.model.callbacks.stringCallback;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.model.observers.MessagesObserver;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * A public interface implemented by the BackendDataHandler.
 * Should be implemented by a class which somehow communicates with the database.
 * Used by BackendFactory, UserRepository, AdvertRepository and RepositoryFactory.
 * Written by masthuggis
 */
public interface iBackend {
    void deleteAd(List<Map<String, String>> chatReceiverAndUserIDMap, Map<String, String> adIDAndUserID);

    void removeChat(String userID, String chatID);

    void updateAdToFirebase(File imageFile, Map<String, Object> dataMap);

    void writeAdvertToFirebase(File imageFile, Map<String, Object> dataMap);

    void addAdToFavourites(String adID, String userID);

    void removeAdFromFavourites(String adID, String userID);

    void getFavouriteIDs(String userID, DBMapCallback dbMapCallback);

    void deleteIDFromFavourites(String id, String favouriteID);

    void userSignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback);

    void getUserChats(String userID, DBCallback DBCallback, FailureCallback failureCallback);

    void getUser(DBMapCallback dbMapCallback);

    void getMessages(String uniqueChatID, DBCallback messageCallback);

    void createNewChat(String adOwnerID, String adBuyerID, String advertID, String imageURL, stringCallback stringCallback);

    void writeMessage(String uniqueChatID, Map<String, Object> messageMap);

    void signOut();

    void addChatObserver(ChatObserver chatObserver);

    void removeChatObserver(ChatObserver chatObserver);

    void addMessagesObserver(MessagesObserver messagesObserver);

    void removeMessagesObserver(MessagesObserver messagesObserver);

    void initialAdvertFetch(DBCallback dbCallback);

    void attachMarketListener(DBCallback DBCallback);

    boolean isUserSignedIn();

    void userSignUpAndSignIn(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback);

    void getUserFromID(String userID, DBMapCallback dbMapCallback);

    boolean marketListenerSet();
}

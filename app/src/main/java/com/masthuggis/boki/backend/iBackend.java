package com.masthuggis.boki.backend;

import com.masthuggis.boki.backend.callbacks.DBCallback;
import com.masthuggis.boki.backend.callbacks.DBMapCallback;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.observers.BackendObserver;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface iBackend {
    void deleteAd(List<Map<String, String>> chatReceiverAndUserIDMap, Map<String, String> adIDAndUserID);

    void removeChat(String userID, String chatID);

    void updateAdToFirebase(File imageFile, Map<String, Object> dataMap);

    void writeAdvertToFirebase(File imageFile, Map<String, Object> dataMap);

    void addAdToFavourites(String adID, String userID);

    void removeAdFromFavourites(String adID, String userID);

    void getFavouriteIDs(DBMapCallback dbMapCallback);

    void deleteIDFromFavourites(String favouriteID);

    void userSignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback);

    void getUserChats(String userID, DBCallback DBCallback, FailureCallback failureCallback);

    void getUser(DBMapCallback dbMapCallback);

    void getMessages(String uniqueChatID, DBCallback messageCallback);

    void createNewChat(String adOwnerID, String adBuyerID, String advertID, String imageURL, stringCallback stringCallback);

    void writeMessage(String uniqueChatID, Map<String, Object> messageMap);

    void setUsername(String username, SuccessCallback successCallback);

    void signOut();

    void addBackendObserver(BackendObserver backendObserver);

    void readAllAdvertData(DBCallback DBCallback);

    void removeBackendObserver(BackendObserver backendObserver);

    boolean isUserSignedIn();

    void userSignUpAndSignIn(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback);

    void getUserFromID(String userID, DBMapCallback dbMapCallback);

}

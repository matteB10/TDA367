package com.masthuggis.boki.backend;

import com.masthuggis.boki.backend.callbacks.DBCallback;
import com.masthuggis.boki.backend.callbacks.DBMapCallback;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.FavouriteIDsCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.Chat;
import com.masthuggis.boki.model.observers.BackendObserver;

import java.io.File;
import java.util.HashMap;

public interface iBackend {
    void deleteAd(String uniqueID);

    void updateAdToFirebase(File imageFile, HashMap<String, Object> dataMap);

    void writeAdvertToFirebase(File imageFile, HashMap<String, Object> dataMap);

    void addAdToFavourites(String adID, String userID);

    void removeAdFromFavourites(String adID, String userID);

    void getFavouriteIDs(DBMapCallback dbMapCallback);

    void deleteIDFromFavourites(String favouriteID);

    void userSignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback);

    void getUserChats(String userID, DBCallback DBCallback,FailureCallback failureCallback);

    void getUser(DBMapCallback dbMapCallback);

    void getMessages(String uniqueChatID, Chat chat, DBCallback messageCallback);

    void createNewChat(String uniqueOwnerID, String advertID, stringCallback stringCallback, String receiverUsername);

    void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap);

    void setUsername(String username, SuccessCallback successCallback);

    void signOut();

    void addBackendObserver(BackendObserver backendObserver);

    void readAllAdvertData(DBCallback DBCallback);

    void removeBackendObserver(BackendObserver backendObserver);

    boolean isUserSignedIn();

    void userSignUpAndSignIn(String email, String password,String username,SuccessCallback successCallback, FailureCallback failureCallback);

}

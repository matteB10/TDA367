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

public interface iRepository {

    void saveAdvert(File imageFile, Advertisement ad);

    void fetchAllAdverts(advertisementCallback advertisementCallback);

    void deleteAd(List<Map<String, String>> chatReceiverAndUserIDMap, Map<String, String> adIDAndUserID);



    void addBackendObserver(BackendObserver backendObserver);

    void removeBackendObserver(BackendObserver backendObserver);

    void addToFavourites(String adID, String userID);

    void signIn(String email, String password, SuccessCallback successCallback,
                FailureCallback failureCallback);

    void signUp(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback);

    boolean isUserLoggedIn();

    void getUserChats(String userID, chatCallback chatCallback);

    void getMessages(String uniqueChatID, messagesCallback messagesCallback);

    void createNewChat(String uniqueOwnerID, String receiverUsername, String advertID,String imageURL, stringCallback stringCallback);

    void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap);

    void signOut();

    void getUser(userCallback userCallback);

    void removeChat(String userID, String chatID);

    void getUserFavourites(FavouriteIDsCallback favouriteIDsCallback);
     void removeFromFavourites(String adID, String userID);

    void deleteIDFromFavourites(String favouriteID);

    void updateAdvert(File imageFile, Advertisement ad);
}

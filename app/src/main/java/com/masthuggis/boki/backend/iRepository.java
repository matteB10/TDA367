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

public interface iRepository {

    void saveAdvert(File imageFile, Advertisement ad);

    void fetchAllAdverts(advertisementCallback advertisementCallback);

    void deleteAd(String adID, String userID, List<String> chatIDs);

    void updateAd(String adID, String newTitle, long newPrice, String newDescription,
                  List<String> newTagList, String newCondition, File imageFile);

    void addBackendObserver(BackendObserver backendObserver);

    void removeBackendObserver(BackendObserver backendObserver);

    void addToFavourites(String adID, String userID);

    void signIn(String email, String password, SuccessCallback successCallback,
                FailureCallback failureCallback);

    void signUp(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback);

    boolean isUserLoggedIn();

    void getUserChats(String userID, chatCallback chatCallback);

    void getMessages(String uniqueChatID, messagesCallback messagesCallback);

    void createNewChat(String uniqueOwnerID, String receiverUsername, String advertID, stringCallback stringCallback);

    void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap);

    void signOut();

    void getUser(userCallback userCallback);

    void removeChat(String userID, String chatID);

    void getUserFavourites(FavouriteIDsCallback favouriteIDsCallback);

}

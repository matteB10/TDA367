package com.masthuggis.boki.backend;

import com.masthuggis.boki.backend.callbacks.DBCallback;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.Chat;
import com.masthuggis.boki.model.observers.BackendObserver;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface iBackend {
    void deleteAd(String uniqueID);

    void updateAd(String adID, String newTitle, Long newPrice, String newDescription, List<String> newTagList, String newCondition, File imageFile, SuccessCallback successCallback);

    void writeAdvertToFirebase(File imageFile, HashMap<String, Object> dataMap, SuccessCallback successCallback);

    void readUserIDAdverts(DBCallback DBCallback, String userID);


    void addAdToFavourites(String adID, String userID);

    void userSignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback);

    void getUserChats(String userID, DBCallback DBCallback);

    Map<String, String> getUser();

    void getMessages(String uniqueChatID, Chat chat, DBCallback messageCallback);

    void createNewChat(String uniqueOwnerID, String advertID, stringCallback stringCallback, String receiverUsername);

    void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap);

    void setUsername(String username, SuccessCallback successCallback);

    void signOut();

    void addBackendObserver(BackendObserver backendObserver);

    void readAllAdvertData(DBCallback DBCallback);

    void removeBackendObserver(BackendObserver backendObserver);

    boolean isUserSignedIn();

    void uploadImageToFirebase(File imageFile, String uniqueAdID, SuccessCallback successCallback);

    void userSignUp(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback);

}

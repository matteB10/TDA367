package com.masthuggis.boki.backend;

import androidx.annotation.Nullable;

import com.masthuggis.boki.backend.callbacks.DBCallback;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.chatDBCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.BackendObserver;
import com.masthuggis.boki.model.Chat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public interface iBackend {
    void deleteAd(String uniqueID);

    void editTitle(String adID, String newTitle);

    void editPrice(String adID, String newPrice);


    void writeAdvertToFirebase(File imageFile, HashMap<String, Object> dataMap, @Nullable SuccessCallback callback);

    void readUserIDAdverts(DBCallback DBCallback, String userID);

    String getFireBaseID(String userID, String advertID);

    void userSignUp(String email, String password, SuccessCallback successCallback);

    void userSignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback);

    void getUserChats(String userID, chatDBCallback chatDBCallback);

    Map<String, String> getUser();

    String getUserID();

    void getMessages(String uniqueChatID, Chat chat, DBCallback messageCallback);

    void createNewChat(HashMap<String, Object> newChatMap, Advertisement advertisement, stringCallback stringCallback);

    void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap);

    void setUsername(String username, SuccessCallback successCallback);

    void signOut();

    void addBackendObserver(BackendObserver backendObserver);

    void readAllAdvertData(DBCallback DBCallback);


}

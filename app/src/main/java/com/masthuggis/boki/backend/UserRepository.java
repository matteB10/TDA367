package com.masthuggis.boki.backend;

import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.chatCallback;
import com.masthuggis.boki.backend.callbacks.messagesCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.backend.callbacks.userCallback;
import com.masthuggis.boki.model.ChatFactory;
import com.masthuggis.boki.model.MessageFactory;
import com.masthuggis.boki.model.UserFactory;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.model.iMessage;
import com.masthuggis.boki.model.iUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserRepository is a repository which is used for retrieving information related to the user of the application.
 * Information like current user, current users chats, messages, etc.
 */
public class UserRepository {

    private iBackend backend;

    UserRepository(iBackend backend) {
        this.backend = backend;
    }

    /**
     * @param email           email used for signing in.
     * @param password        password which needs to match the password belonging to email.
     * @param successCallback A callback which indicates what should happen if the login is successful.
     * @param failureCallback A callback which indicates what should happen if the login failed.
     *                        <p>
     *                        This method asks the backend to try and log in. Depending on its success
     *                        it either tells the model to log in or not.
     */

    void signIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        backend.userSignIn(email, password, successCallback, failureCallback);
    }

    /**
     * @param email           email used for signing up.
     * @param password        password which needs to match the password belonging to email.
     * @param successCallback A callback which indicates what should happen if the login is successful.
     */

    void signUp(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback) {
        backend.userSignUpAndSignIn(email, password, username, successCallback, failureCallback);
    }


    void getUser(userCallback userCallback) {
        backend.getUser(dataMap -> {

            iUser user = UserFactory.createUser(dataMap.get("email").toString(), dataMap.get("username").toString(), dataMap.get("userID").toString());
            userCallback.onCallback(user);
        });

    }

    boolean isUserLoggedIn() {
        return backend.isUserSignedIn();
    }

    void getUserChats(String userID, chatCallback chatCallback) {
        backend.getUserChats(userID, chatMap -> {
            List<iChat> chatList = new ArrayList<>();
            for (Map<String, Object> map : chatMap) {
                List<iUser> userList = new ArrayList<>();
                createUser(map.get("userOneID").toString(), newUser -> {
                    userList.add(newUser);
                    createUser(map.get("userTwoID").toString(), newUser1 -> {
                        userList.add(newUser1);
                        chatList.add(ChatFactory.createChat(map.get("uniqueChatID").toString()
                                , userList.get(0), userList.get(1), map.get("advertID").toString(), map.get("imageURL").toString(), (boolean) map.get("isActive")));
                        if (chatList.size() == chatMap.size()) {
                            chatCallback.onCallback(chatList);
                        }
                    });
                });

            }
        }, errorMessage -> chatCallback.onCallback(new ArrayList<>()));
    }

    private void createUser(String userID, userCallback userCallback) {
        backend.getUserFromID(userID, dataMap -> userCallback.onCallback(UserFactory.createUser(dataMap.get("email").toString(), dataMap.get("username").toString(), dataMap.get("userID").toString())));
    }

    public void getMessages(String uniqueChatID, messagesCallback messagesCallback) {
        List<iMessage> messages = new ArrayList<>();

        backend.getMessages(uniqueChatID, advertDataList -> {
            if (advertDataList.size() == 0) {
                return;
            }
            messages.clear();
            for (Map<String, Object> objectMap : advertDataList) {
                messages.add(MessageFactory.createMessage(objectMap.get("message")
                        .toString(), Long.parseLong(objectMap.get("timeSent").toString()), objectMap.get("sender").toString()));
            }

        });
        messagesCallback.onCallback(messages);

    }


    void createNewChat(String adOwnerID, String adBuyerID, String advertID, String imageURL, stringCallback stringCallback) {
        backend.createNewChat(adOwnerID, adBuyerID, advertID, imageURL, stringCallback);
    }

    void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        backend.writeMessage(uniqueChatID, messageMap);
    }

    private void setUsername(String username, SuccessCallback successCallback) {
        backend.setUsername(username, successCallback);
    }

    void signOut() {
        backend.signOut();
    }

    void removeChat(String userID, String chatID) {
        backend.removeChat(userID, chatID);
    }

    void deleteIDFromFavourites(String id, String favouriteID) {
        backend.deleteIDFromFavourites(id,favouriteID);
    }
}


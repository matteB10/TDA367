package com.masthuggis.boki.model;

import com.masthuggis.boki.model.callbacks.FailureCallback;
import com.masthuggis.boki.model.callbacks.SuccessCallback;
import com.masthuggis.boki.model.callbacks.chatCallback;
import com.masthuggis.boki.model.callbacks.messagesCallback;
import com.masthuggis.boki.model.callbacks.stringCallback;
import com.masthuggis.boki.model.callbacks.userCallback;
import com.masthuggis.boki.backend.iBackend;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.model.observers.MessagesObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class providing the functionality to convert data from backend into objects to be used
 * by the domain-layer of the application.
 * Data is fetched through the iBackend interface.
 * For methods that are delegating, check the class which is delegated to for comments.
 * Used by Repository and RepositoryFactory.
 * Written by masthuggis
 */

 class UserRepository {

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

    /**
     * Calls method from backend to get current user.
     * On callback from backend method call creates a new user which is used in model.
     *
     * @param userCallback callback used to return a user after and the conversion to user is done.
     */
    void getCurrentUser(userCallback userCallback) {
        backend.getUser(dataMap -> {

            iUser user = UserFactory.createUser(dataMap.get("email").toString(), dataMap.get("username").toString(), dataMap.get("userID").toString());
            userCallback.onCallback(user);
        });

    }

    boolean isUserLoggedIn() {
        return backend.isUserSignedIn();
    }

    /**
     * Creates on callback from backend by using a factory for the chats. Uses helper methods to get the information
     * needed from the backend to get all needed parameters for creating chats.
     * @param userID current user ID.
     * @param chatCallback callback used to return a list of chats.
     */
    void getUserChats(String userID, chatCallback chatCallback) {
        backend.getUserChats(userID, chatMap -> {
            List<iChat> chatList = new ArrayList<>();
            for (Map<String, Object> map : chatMap) {
                List<iUser> userList = new ArrayList<>();
                createUsersForChat(map.get("userOneID").toString(), map.get("userTwoID").toString(), userList, () -> {
                    chatList.add(ChatFactory.createChat(map.get("uniqueChatID").toString()
                            , userList.get(0), userList.get(1), map.get("advertID").toString(), map.get("imageURL").toString(), (boolean) map.get("isActive")));
                    if (chatList.size() == chatMap.size()) {
                        chatCallback.onCallback(chatList);
                    }
                });
            }
        }, errorMessage -> chatCallback.onCallback(new ArrayList<>()));
    }

    /**
     * Helper method to get two users from database needed to create a chat.
     * @param userIDOne user id of participant one.
     * @param userIDTwo user id of participant two.
     * @param users list to add the created users to.
     * @param successCallback callback to tell method caller to proceed on success.
     */
    private void createUsersForChat(String userIDOne, String userIDTwo, List<iUser> users, SuccessCallback successCallback) {
        fetchUserFromDB(userIDOne, users, () -> fetchUserFromDB(userIDTwo, users, successCallback::onSuccess));
    }

    /**
     * Helper method to get create a user from database.
     * @param userID id of user to fetch.
     * @param users list to add new user to.
     * @param successCallback tells method caller to proceed on success.
     */
    private void fetchUserFromDB(String userID, List<iUser> users, SuccessCallback successCallback) {
        backend.getUserFromID(userID, dataMap -> {
            users.add((UserFactory.createUser(dataMap.get("email").toString(), dataMap.get("username").toString(), dataMap.get("userID").toString())));
            successCallback.onSuccess();
        });
    }

    /**
     * Calls backend to fetch information needed to fetch messages. Adds created messages to a list
     * which it then returns through a messagesCallback.
     * @param uniqueChatID id needed for backend to find correct chat.
     * @param messagesCallback used to return a list of messages when fetch is done.
     */
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

    void signOut() {
        backend.signOut();
    }

    void removeChat(String userID, String chatID) {
        backend.removeChat(userID, chatID);
    }

    void deleteIDFromFavourites(String id, String favouriteID) {
        backend.deleteIDFromFavourites(id, favouriteID);
    }

    void addChatObserver(ChatObserver chatObserver) {
        backend.addChatObserver(chatObserver);
    }

    void removeChatObserver(ChatObserver chatObserver) {
        backend.removeChatObserver(chatObserver);
    }

    void removeMessagesObserver(MessagesObserver messagesObserver) {
        backend.removeMessagesObserver(messagesObserver);
    }

    void addMessagesObserver(MessagesObserver messagesObserver) {
        backend.addMessagesObserver(messagesObserver);
    }
}


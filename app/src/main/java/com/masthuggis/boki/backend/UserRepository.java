package com.masthuggis.boki.backend;

import com.masthuggis.boki.backend.callbacks.DBCallback;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.chatCallback;
import com.masthuggis.boki.backend.callbacks.chatDBCallback;
import com.masthuggis.boki.backend.callbacks.messagesCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.Chat;
import com.masthuggis.boki.model.DataModel;
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
     *
     * @param email email used for signing in.
     * @param password password which needs to match the password belonging to email.
     * @param successCallback A callback which indicates what should happen if the login is successful.
     * @param failureCallback A callback which indicates what should happen if the login failed.
     *
     *                        This method asks the backend to try and log in. Depending on its success
     *                        it either tells the model to log in or not.
     */

    public void signIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        backend.userSignIn(email, password, () -> {
            logUserIn();
            successCallback.onSuccess();
        }, failureCallback::onFailure);
    }

    /**
     *
     * @param email email used for signing up.
     * @param password password which needs to match the password belonging to email.
     * @param successCallback A callback which indicates what should happen if the login is successful.
     */

    public void signUp(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        backend.userSignUp(email, password, successCallback, failureCallback);
    }

    public void signInAfterRegistration(String email, String password, String username) {
        backend.userSignIn(email, password, new SuccessCallback() {
            @Override
            public void onSuccess() {
                UserRepository.this.setUsername(username, new SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        UserRepository.this.logUserIn();
                    }
                });
            }
        }, errorMessage -> {

        });
    }

    public void logUserIn() {
        iUser user;
        Map<String, String> map = backend.getUser();
        user = UserFactory.createUser(map.get("email"), map.get("username"), map.get("userID"));
        DataModel.getInstance().loggedIn(user);
    }

    public boolean isUserLoggedIn() {
        return backend.isUserSignedIn();
    }


    private void loggedOut() {
        DataModel.getInstance().loggedOut();

    }

    public void getUserChats(String userID, chatCallback chatCallback) {


        backend.getUserChats(userID, new chatDBCallback() {
            @Override
            public void onCallback(List<Map<String, Object>> chatMap) {
                List<iChat> chatList = new ArrayList<>();
                for (Map<String, Object> map : chatMap) {
                    Advertisement ad = createAdFromMap(map);
                    chatList.add(ChatFactory.createChat(map.get("uniqueChatID").toString(),ad,map.get("receiver").toString(),map.get("sender").toString()));
                }
                chatCallback.onCallback(chatList);

            }
        });
    }

    private Advertisement createAdFromMap(Map<String, Object> dataMap) {

        String title = "" + (String) dataMap.get("title");
        String description = (String) dataMap.get("description");
        long price = (long) dataMap.get("price");
        List<String> tags = (List<String>) dataMap.get("tags");
        String uniqueOwnerID = (String) dataMap.get("uniqueOwnerID");
        Advert.Condition condition = Advert.Condition.valueOf((String) dataMap.get("condition"));
        String uniqueAdID = (String) dataMap.get("uniqueAdID");
        String datePublished = (String) dataMap.get("date");
        String imageURL= (String) dataMap.get("imgURL");
        String owner = (String) dataMap.get("advertOwner");

        return AdFactory.createAd(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition, imageURL, tags,owner);
    }






    public String userID() {
        return backend.getUserID();
    }

/*
    public String userEmail() {
        return BackendDataHandler.getInstance().getUserEmail();
    }

 */

    public void getMessages(String uniqueChatID, Chat chat, messagesCallback messagesCallback) {
        List<iMessage> messages = new ArrayList<>();

        backend.getMessages(uniqueChatID, chat, new DBCallback() {
            @Override
            public void onCallBack(List<Map<String, Object>> advertDataList) {
                if (advertDataList.size() == 0) {
                    return;
                }
                messages.clear();
                for (Map<String, Object> objectMap : advertDataList) {
                    messages.add(MessageFactory.createMessage(objectMap.get("message")
                            .toString(), Long.parseLong(objectMap.get("timeSent").toString()), objectMap.get("sender").toString()));
                }

            }
        });
        messagesCallback.onCallback(messages);

    }


    public void createNewChat(HashMap<String, Object> newChatMap, Advertisement advertisement, stringCallback stringCallback) {
        backend.createNewChat(newChatMap,advertisement,stringCallback);
    }

    public void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        backend.writeMessage(uniqueChatID, messageMap);
    }

    private void setUsername(String username,SuccessCallback successCallback) {
        backend.setUsername(username, successCallback);
    }

    public void signOut() {
        backend.signOut();
    }
}


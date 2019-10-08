package com.masthuggis.boki.backend;

import androidx.annotation.Nullable;

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

public class UserRepository {
    private static UserRepository userRepository;

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    public void signIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        BackendDataHandler.getInstance().userSignIn(email, password, () -> {
            logUserIn();
            successCallback.onSuccess();
        }, failureCallback::onFailure);
    }


    public void signUp(String email, String password, SuccessCallback successCallback) {
        BackendDataHandler.getInstance().userSignUp(email, password, successCallback::onSuccess);
    }

    public void signInAfterRegistration(String email, String password, String username) {
        BackendDataHandler.getInstance().userSignIn(email, password, new SuccessCallback() {


            @Override
            public void onSuccess() {
                setUsername(username, new SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        logUserIn();

                    }
                });
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(@Nullable String errorMessage) {

            }
        });
    }

    public void logUserIn() {
        iUser user;
        Map<String, String> map = BackendDataHandler.getInstance().getUser();
        user = UserFactory.createUser(map.get("email"), map.get("username"), map.get("userID"));
        DataModel.getInstance().loggedIn(user);
    }

    public boolean isUserLoggedIn() {
        return BackendDataHandler.getInstance().isUserSignedIn();
    }


    private void loggedOut() {
        DataModel.getInstance().loggedOut();

    }

    public void getUserChats(String userID, chatCallback chatCallback) {


        BackendDataHandler.getInstance().getUserChats(userID, new chatDBCallback() {
            @Override
            public void onCallback(List<Map<String, Object>> chatMap) {
                List<iChat> chatList = new ArrayList<>();
                for (Map<String, Object> map : chatMap) {
                    Advertisement ad = createAdFromMap(map);
                    chatList.add(ChatFactory.createChat(map.get("uniqueChatID").toString(),ad));
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
        String owner = (String) dataMap.get("advertOwnerID");

        return AdFactory.createAd(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition, imageURL, tags,owner);
    }






    public String userID() {
        return BackendDataHandler.getInstance().getUserID();
    }

/*
    public String userEmail() {
        return BackendDataHandler.getInstance().getUserEmail();
    }

 */

    public void getMessages(String uniqueChatID, Chat chat, messagesCallback messagesCallback) {
        List<iMessage> messages = new ArrayList<>();

        BackendDataHandler.getInstance().getMessages(uniqueChatID, chat, new DBCallback() {
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


    public void createNewChat(HashMap<String, Object> newChatMap,Advertisement advertisement,stringCallback stringCallback) {
        BackendDataHandler.getInstance().createNewChat(newChatMap,advertisement,stringCallback);
    }

    public void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        BackendDataHandler.getInstance().writeMessage(uniqueChatID, messageMap);
    }

    public void setUsername(String username,SuccessCallback successCallback) {
        BackendDataHandler.getInstance().setUsername(username, successCallback);
    }

    public void signOut() {
        BackendDataHandler.getInstance().signOut();
    }
}


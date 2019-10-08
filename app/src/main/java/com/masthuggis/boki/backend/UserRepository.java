package com.masthuggis.boki.backend;

import androidx.annotation.Nullable;

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
            loggedIn();
            successCallback.onSuccess();
        }, errorMessage -> failureCallback.onFailure(errorMessage));
    }


    public void signUp(String email, String password, SuccessCallback successCallback) {
        BackendDataHandler.getInstance().userSignUp(email, password, successCallback::onSuccess);


    }

    public void signInAfterRegistration(String email, String password, String username) {
        BackendDataHandler.getInstance().userSignIn(email, password, new SuccessCallback() {


            @Override
            public void onSuccess() {
                setUsername(username);
                loggedIn();
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(@Nullable String errorMessage) {

            }
        });
    }

    private void loggedIn() {
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

        List<iChat> chatList = new ArrayList<>();

        BackendDataHandler.getInstance().getUserChats(userID, new chatDBCallback() {
            @Override
            public void onCallback(List<Map<String, Object>> chatMap) {
                for (Map<String, Object> map : chatMap) {
                    chatList.add(ChatFactory.createChat(map.get("sender").toString(), map.get("receiver").toString(), map.get("uniqueChatID").toString(), map.get("receiverUsername").toString()));
                }
                chatCallback.onCallback(chatList);

            }
        });
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
                            .toString(), objectMap.get("timeSent").toString(), objectMap.get("sender").toString()));
                }

            }
        });
        messagesCallback.onCallback(messages);
    }


    public void createNewChat(HashMap<String, Object> newChatMap) {
        BackendDataHandler.getInstance().createNewChat(newChatMap);
    }

    public void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        BackendDataHandler.getInstance().writeMessage(uniqueChatID, messageMap);
    }

    public void setUsername(String username) {
        BackendDataHandler.getInstance().setUsername(username);
    }

    public void signOut() {
        BackendDataHandler.getInstance().signOut();
    }
}


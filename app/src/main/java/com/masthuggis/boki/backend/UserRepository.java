package com.masthuggis.boki.backend;

import com.google.api.Backend;
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

    /**
     * Method to sign a user in using a email address and a chosen password.
     *
     * @param email
     * @param password
     */
    public void signIn(String email, String password) {
        BackendDataHandler.getInstance().userSignIn(email, password);
        loggedIn();
        BackendDataHandler.getInstance().userSignIn(email,password);
    }

    /**
     * Method to sign up a user with a email address and a chosen password,
     * connected to Firebase.
     *
     * @param email
     * @param password
     */
    public void signUp(String email, String password) {
        BackendDataHandler.getInstance().userSignUp(email, password);
        loggedOut();
    }
    /**Checks if a user is sign in to the app.
     * @return true if a user is signed in
     */

    private void loggedIn() {
        iUser user;
        Map<String, String> map = BackendDataHandler.getInstance().getUser();
        user = UserFactory.createUser(map.get("userID"));
        DataModel.getInstance().loggedIn(user);
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
                    chatList.add(ChatFactory.createChat(map.get("sender").toString(), map.get("receiver").toString(),map.get("uniqueChatID").toString()));
                }
                chatCallback.onCallback(chatList);
            }
        });
    }

    public void getMessages(String uniqueChatID, Chat chat, messagesCallback messagesCallback) {
        List<iMessage> messages = new ArrayList<>();

        BackendDataHandler.getInstance().getMessages(uniqueChatID,chat, new DBCallback() {
            @Override
            public void onCallBack(List<Map<String, Object>> advertDataList) {
                if(advertDataList.size() ==0){
                    return;
                }
                messages.clear();
                for(Map<String,Object> objectMap : advertDataList){
                    messages.add(MessageFactory.createMessage(objectMap.get("message")
                            .toString(),objectMap.get("timeSent").toString(),objectMap.get("sender").toString()));
                }

            }
        }); messagesCallback.onCallback(messages);
    }


    public void createNewChat(HashMap<String,Object> newChatMap) {
        BackendDataHandler.getInstance().createNewChat(newChatMap);
    }
    public void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap){
        BackendDataHandler.getInstance().writeMessage(uniqueChatID,messageMap);
    }
}


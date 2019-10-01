package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.model.iUser;

import java.util.ArrayList;
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


    public void signIn(String email, String password) {
        BackendDataHandler.getInstance().userSignIn(email, password);
        loggedIn();
    }

    public void signUp(String email, String password) {
        BackendDataHandler.getInstance().userSignUp(email, password);
        loggedOut();
    }

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

        BackendDataHandler.getInstance().readChatData(userID, new chatDBCallback() {
            @Override
            public void onCallback(List<Map<String, Object>> chatMap) {
                for (Map<String, Object> map : chatMap) {
                    chatList.add(ChatFactory.createChat(map.get("sender").toString(), map.get("receiver").toString()));
                }
                chatCallback.onCallback(chatList);
            }
        });
    }




}


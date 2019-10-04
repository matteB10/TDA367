package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class User implements iUser {
    private String email;
    private String displayname;
    private String userID;
    private List<iChat> chats;
    iFavoriteCollection favoriteCollection;

    public User(String email, String displayname, String userID) {
        this.email = email;
        this.displayname = displayname;
        this.userID = userID;
      //  UserRepository.getInstance().getUserChats(userID, chatsList -> chats = chatsList);
        UserRepository.getInstance().getUserChats(userID, chatsList -> {
            chats = null;
            chats = new ArrayList<>(chatsList);
        });
    }


    public String getId() {
        return this.userID;
    }

    @Override
    public String getDisplayName() {
        return this.displayname;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public List<iChat> getChats() {
        return this.chats;
    }
}

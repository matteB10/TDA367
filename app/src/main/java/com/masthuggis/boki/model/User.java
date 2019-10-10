package com.masthuggis.boki.model;

import java.util.ArrayList;
import java.util.List;

public class User implements iUser {
    private String email;
    private String displayname;
    private String userID;
    private List<iChat> chats;
    private DataModel dataModel;
    private iFavoriteCollection favoriteCollection;

    public User(String email, String displayname, String userID,DataModel dataModel) {
        this.email = email;
        this.displayname = displayname;
        this.userID = userID;
        this.dataModel = dataModel;
        dataModel.fetchUserChats(userID, chatsList -> {
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

package com.masthuggis.boki.model;

import java.util.List;

public class User implements iUser {
    private String email;
    private String displayname;
    private String userID;
    private List<iChat> chats;
    private List<Advertisement> advertisements;
    private iFavoriteCollection favoriteCollection;

    public User(String email, String displayname, String userID) {
        this.email = email;
        this.displayname = displayname;
        this.userID = userID;
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

    @Override
    public void setChats(List<iChat> chats) {
        this.chats = chats;
    }
}

package com.masthuggis.boki.model;

import java.util.List;

public class User implements iUser {
    private  String email;
    private  String displayname;
    private  String userID;
    private List<iChat> chats;
    iFavoriteCollection favoriteCollection;

    public User(String email, String displayname, String userID) {
        this.email = email;
        this.displayname= displayname;
        this.userID = userID;
    }

    public String getId() {
        return this.userID;
    }
}

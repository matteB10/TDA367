package com.masthuggis.boki.model;

import java.util.List;

public class User implements iUser {
    private List<iChat> chats;
    private String id;
    iFavoriteCollection favoriteCollection;

    public String getId() {
        return this.id;
    }
    public void setId(String id){
        this.id =id;
    }
}

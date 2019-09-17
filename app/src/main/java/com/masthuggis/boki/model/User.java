package com.masthuggis.boki.model;

import java.util.List;

public class User implements iUser {
    private List<iChat> chats;
    private int id;
    iFavoriteCollection favoriteCollection;

    public int getId() {
        return this.id;
    }
}

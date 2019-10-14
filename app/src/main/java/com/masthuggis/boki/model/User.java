package com.masthuggis.boki.model;

import java.util.List;

public class User implements iUser {
    private String email;
    private String displayname;
    private String userID;
    private List<iChat> chats;
    private List<Advertisement> advertisements;
    private iFavoriteCollection favoriteCollection;

     User(String email, String displayname, String userID) {
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

    @Override
    public void setAdverts(List<Advertisement> adverts) {
        this.advertisements = adverts;
    }

    @Override
    public List<Advertisement> getAdverts() {
        return advertisements;
    }

    @Override
    public String getChatIDFromAdID(String adID) {
         for(iChat chat:chats){
             if(chat.getUniqueIDAdID().equals(adID)){
                 return chat.getChatID();
             }
         }
         return "";

    }
}

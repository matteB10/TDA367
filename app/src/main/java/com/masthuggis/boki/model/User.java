package com.masthuggis.boki.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by UserFactory.
 * Written by masthuggis
 */
public class User implements iUser {
    private String email;
    private String displayname;
    private String userID;
    private List<iChat> chats;
    private List<Advertisement> advertisements;
    private List<Advertisement> favourites;

    User(String email, String displayname, String userID) {
        this.email = email;
        this.displayname = displayname;
        this.userID = userID;
        this.favourites = new ArrayList<>(); //Will probably not remain as this list, new list will be set
    }

    public void addFavourite(Advertisement advertisement) {
        favourites.add(advertisement);
    }

    public void removeFavourite(Advertisement advertisement) {
        favourites.remove(advertisement);
    }

    public void setFavourites(List<Advertisement> advertisements) {
        this.favourites = advertisements;
    }

    public List<Advertisement> getFavourites() {
        return this.favourites;
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
}

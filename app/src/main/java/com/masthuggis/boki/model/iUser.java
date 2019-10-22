package com.masthuggis.boki.model;

import java.util.List;

public interface iUser {
    String getId();

    String getDisplayName();

    String getEmail();

    List<iChat> getChats();

    void setChats(List<iChat> chats);

    void setAdverts(List<Advertisement> adverts);

    List<Advertisement> getAdverts();

    void addFavourite(Advertisement advertisement);

    void removeFavourite(Advertisement advertisement);

    void setFavourites(List<Advertisement> advertisements);

    List<Advertisement> getFavourites();
}

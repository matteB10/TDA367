package com.masthuggis.boki.model;

import java.util.List;

public interface iChat {
    List<iMessage> getMessages();

    String getReceiverID();

    String getChatID();

    String getReceiverUsername();

    String getSenderUsername();

    String getSenderID();

    long timeLastMessageSent();

    Advertisement getAdvert();

    String getDisplayName();
}

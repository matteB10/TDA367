package com.masthuggis.boki.model;

import java.util.List;

public interface iChat {
    List<iMessage> getMessages();

    String getChatID();

    long timeLastMessageSent();

    String getDisplayName(String currentUserID);

    String getUniqueIDAdID();

    void setMessages(List<iMessage> messagesList);
}

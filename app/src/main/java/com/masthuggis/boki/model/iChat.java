package com.masthuggis.boki.model;

import java.util.List;

public interface iChat {
    List<iMessage> getMessages();

    String getReceiver();
    String getChatID();
     void removeChatObserver(ChatObservers chatObserver);
     void addChatObserver( ChatObservers chatObserver);
     String getReceiverUsername();


    String timeLastMessageSent();
}

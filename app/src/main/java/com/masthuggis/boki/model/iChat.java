package com.masthuggis.boki.model;

import java.util.List;

public interface iChat {
    List<iMessage> getMessages();

    String getReceiver();
    String getChatID();
     String getReceiverUsername();


    long timeLastMessageSent();

    Advertisement getAdvert();
}

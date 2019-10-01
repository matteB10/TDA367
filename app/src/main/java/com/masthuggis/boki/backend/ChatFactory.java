package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Chat;
import com.masthuggis.boki.model.iChat;

public class ChatFactory {

    public static iChat createChat(String sender, String receiver){
        return new Chat( sender, receiver);
    }
}

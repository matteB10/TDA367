package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.Chat;
import com.masthuggis.boki.model.iChat;

public class ChatFactory {

    public static iChat createChat(String uniqueChatID,Advertisement advertisement){
        return new Chat( uniqueChatID,advertisement);
    }
}

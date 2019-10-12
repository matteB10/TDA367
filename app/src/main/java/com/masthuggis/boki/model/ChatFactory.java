package com.masthuggis.boki.model;

/**
 * Factory for creating objects of iChat-type.
 */

public class ChatFactory {

    public static iChat createChat(String uniqueChatID, iUser sender, iUser receiver, String uniqueAdID) {
        return new Chat(uniqueChatID, sender, receiver, uniqueAdID);


    }
}

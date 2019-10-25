package com.masthuggis.boki.model;

/**
 * Factory for creating objects of iChat-type.
 * Used by UserRepository
 * Written by masthuggis
 */

public class ChatFactory {

    public static iChat createChat(String uniqueChatID, String senderID, String receiverID,String senderUsername,String receiverUsername, String uniqueAdID,String imageURL, boolean isActive) {
        return new Chat(uniqueChatID, senderID, receiverID,senderUsername,receiverUsername ,uniqueAdID,imageURL,isActive);
    }
}

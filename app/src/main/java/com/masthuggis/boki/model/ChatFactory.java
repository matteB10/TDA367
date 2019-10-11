package com.masthuggis.boki.model;

/**
 * Factory for creating objects of iChat-type.
 */

public class ChatFactory {

    public static iChat createChat(String uniqueChatID,String senderID,String receiverID, String uniqueAdID, String receiverUsername, String senderUsername, DataModel dataModel){
        return new Chat( uniqueChatID,senderID,receiverID,uniqueAdID,receiverUsername,senderUsername,dataModel);


        }
}

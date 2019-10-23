package com.masthuggis.boki.model;

/**
 * Factory for creating objects of the iMessage-type.
 * Used by UserRepository
 * Written by masthuggis
 */

public class MessageFactory {

    public static iMessage createMessage(String message, long timeSent, String senderID){
        return new Message(message,timeSent,senderID);
    }
}

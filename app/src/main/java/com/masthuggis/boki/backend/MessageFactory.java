package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Message;
import com.masthuggis.boki.model.iMessage;

public class MessageFactory {

    public static iMessage createMessage(String message, String timeSent,String senderID){
        return new Message(message,timeSent,senderID);
    }
}

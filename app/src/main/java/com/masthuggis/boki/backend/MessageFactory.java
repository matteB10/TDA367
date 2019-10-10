package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Message;
import com.masthuggis.boki.model.iMessage;

/**
 * Factory for creating objects of the iMessage-type.
 */

class MessageFactory {

    static iMessage createMessage(String message, long timeSent, String senderID){
        return new Message(message,timeSent,senderID);
    }
}

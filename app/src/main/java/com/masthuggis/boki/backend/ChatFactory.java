package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Chat;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;

/**
 * Factory for creating objects of iChat-type.
 */

class ChatFactory {

    static iChat createChat(String uniqueChatID,String senderID,String receiverID, String uniqueAdID, String receiverUsername, String senderUsername, DataModel dataModel){
        return new Chat( uniqueChatID,senderID,receiverID,uniqueAdID,receiverUsername,senderUsername,dataModel);


        }
}

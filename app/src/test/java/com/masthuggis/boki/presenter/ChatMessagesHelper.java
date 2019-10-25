package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.ChatFactory;
import com.masthuggis.boki.model.MessageFactory;
import com.masthuggis.boki.model.UserFactory;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.model.iUser;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.util.ArrayList;
import java.util.List;

public class ChatMessagesHelper {
    public static List<iChat> generateUserChats() {
        List<iUser> tenUsers = generate10Users();
        List<iChat> userChats = new ArrayList<>();
        boolean isActive;
        for (int i = 0; i < 5; i++) {
            isActive = i % 2 == 0;
            iChat chat = ChatFactory.createChat(UniqueIdCreator.getUniqueID(), "senderID"+i,"receiverID"+i,"senderUsername"+i
                    ,"receiverUsername"+i, "uniqueAdID" + i, "123", isActive);
            userChats.add(chat);
        }
        return userChats;
    }


    static void addMessagesToChats(List<iChat> chats, int numMessagesToAdd) {
        // hhMMss DDmmYY
        String str = "191016133503";
        long l = Long.parseLong(str);
        int i = 0;
        for (iChat chat : chats) {
            chat.setMessages(new ArrayList<>());
            for (int j = 0; j < numMessagesToAdd; j++) {
                chat.getMessages().add(MessageFactory.createMessage("meddelande" + i, l + j, "jag" + i));
                l++;
                i++;
            }
        }
    }

    static List<iUser> generate10Users() {
        List<iUser> tenUsers = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            tenUsers.add(UserFactory.createUser("mail" + i + "@mail.se", "displayname" + i, "userid" + i));
        }
        return tenUsers;
    }
}

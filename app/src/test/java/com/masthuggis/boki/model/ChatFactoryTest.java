package com.masthuggis.boki.model;

import org.junit.Test;

public class ChatFactoryTest {

    String uniqueChatID = "uniqueChatID";
    String uniqueAdID = "uniqueAdID";
    String imageURL = "imageURL";
    iUser sender = new User("email1", "displayName1", "userID1");
    iUser receiver = new User("email2", "displayName2", "userID2");
    boolean isActive = true;
    iChat chat = new Chat("uniqueChatID", sender, receiver, "uniqueAdID",
            "imageURL", isActive);

    @Test
    public void createChatTest() {
        iChat factoryChat = ChatFactory.createChat(uniqueChatID, sender, receiver, uniqueAdID, imageURL, isActive);
        assert (factoryChat.getChatID().equals(chat.getChatID()));
        assert (factoryChat.getAdID().equals(chat.getAdID()));
        assert (factoryChat.getImageURL().equals(chat.getImageURL()));
        assert (factoryChat.isActive() == chat.isActive());
        assert (factoryChat.getReceiverID(receiver.getId()).equals(chat.getReceiverID(receiver.getId())));
        assert (factoryChat.getReceiverName(receiver.getId()).equals(chat.getReceiverName(receiver.getId())));
    }
}

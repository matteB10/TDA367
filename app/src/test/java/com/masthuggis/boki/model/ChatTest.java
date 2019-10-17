package com.masthuggis.boki.model;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ChatTest {


    @Mock
    private DataModel databaseMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void testTimeLastMessageSent() {
        when(databaseMock.getUserID()).thenReturn("userID");

        Chat chat = new Chat("123123123",null,null,"31321321","imageURL",true);
        chat.setMessages(new ArrayList<>());
        chat.getMessages().add(new Message("meddelande", 123123, "senderID"));
        chat.getMessages().add(new Message("meddelande2", 123, "senderID2"));
        assertEquals(chat.timeLastMessageSent(), 123123);
    }

    @Test
    public void testGetReceiverName() {
        iUser currentUser = UserFactory.createUser("email","MyDisplayName","currentUserID");
        iUser receiverUser = UserFactory.createUser("email2","receiverUser","receiverUserID");

        Chat chat = new Chat("uniqueChatID", currentUser, receiverUser,"123321321123","imageURL", true);

        when(databaseMock.getUserID()).thenReturn(currentUser.getId());
        when(databaseMock.getUserDisplayName()).thenReturn(currentUser.getDisplayName());


        String displayname = chat.getReceiverName(databaseMock.getUserID());

        assertEquals(receiverUser.getDisplayName(),displayname);
    }
}

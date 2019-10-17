package com.masthuggis.boki.model;

import org.junit.Test;

public class MessageFactoryTest {

    iMessage message = new Message("message",123,"senderID");


    @Test
    public void testCreateMessage() {
        iMessage testMessage = MessageFactory.createMessage(message.getMessage(),message.getTimeSent(),message.getSenderID());
        assert (message.getMessage().equals(testMessage.getMessage()));
        assert (message.getTimeSent() == testMessage.getTimeSent());
        assert (message.getSenderID().equals(testMessage.getSenderID()));
    }
}

package com.masthuggis.boki.model;

import org.junit.Test;

public class MessageTest {

    Message message = new Message("message",190921,"senderID");


    @Test
    public void getMessageTest() {
        assert (message.getMessage().equals("message"));
    }

    @Test
    public void getTimeSentTest() {
        assert (message.getTimeSent() == 190921);
    }

    @Test
    public void getSenderIDTest() {
        assert (message.getSenderID().equals("senderID"));
    }

    @Test
    public void setMessageTest() {
        message.setMessage("Rich Purnell is a steely-eyed missile man");
        assert (message.getMessage().equals("Rich Purnell is a steely-eyed missile man"));
    }
}

package com.masthuggis.boki.model;

import java.util.List;

public class Chat implements iChat {
    private List<iMessage> messages;
    private String sender;
    private String receiver;


    public Chat(String sender, String receiver) {
        this.sender =sender;
        this.receiver = receiver;
    }


    public List<iMessage> getMessages() {
        return this.messages;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
}

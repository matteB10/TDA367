package com.masthuggis.boki.model;

import java.util.Date;

public class Message implements iMessage {
    private String message;
    private String timeSent;
    private String senderID;

    public Message(String message, String timeSent, String senderID) {
        this.message= message;
        this.timeSent=timeSent;
        this.senderID=senderID;
    }

    public String setMessage() {
        return this.message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getTimeSent() {
        return this.timeSent;
    }

    @Override
    public String getSenderID(){
        return this.senderID;
    }

}

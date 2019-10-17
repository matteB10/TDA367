package com.masthuggis.boki.model;

public class Message implements iMessage {
    private String message;
    private long timeSent;
    private String senderID;

    Message(String message, long timeSent, String senderID) {
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
    public long getTimeSent() {
        return this.timeSent;
    }

    @Override
    public String getSenderID(){
        return this.senderID;
    }

}

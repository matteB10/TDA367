package com.masthuggis.boki.model;
/**
 *
 * Used by MessageFactory.
 * Written by masthuggis
 */
public class Message implements iMessage {
    private String message;
    private long timeSent;
    private String senderID;

    Message(String message, long timeSent, String senderID) {
        this.message= message;
        this.timeSent=timeSent;
        this.senderID=senderID;
    }

    public void setMessage(String message) {
        this.message = message;
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
    public String getSenderID() {
        return this.senderID;
    }
}

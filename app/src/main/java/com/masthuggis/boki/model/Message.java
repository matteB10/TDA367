package com.masthuggis.boki.model;
/**
 * A class which represents a message.
 *
 * All information needed in this class is when the message was sent,
 * the contents of the message and a sender id.
 * Most interactions with this class is done through the iMessage, which is the
 * interface it implements.
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

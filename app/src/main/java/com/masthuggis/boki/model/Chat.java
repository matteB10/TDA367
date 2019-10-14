package com.masthuggis.boki.model;

import java.util.List;

/**
 * Chat is a java class implementing the iChat interface for the sake of abstraction. Chat is the container of messages which
 * are being sent between two users. The chat contains an advertisementID to make sure that two users can only have one chat considering
 * one specific advertisement.
 */

public class Chat implements iChat {
    private List<iMessage> messages;
    private String chatID;
    private String uniqueAdID;
    private iUser sender;
    private iUser receiver;
    private boolean isActive;


    Chat(String uniqueChatID, iUser sender, iUser receiver, String uniqueAdID, boolean isActive) {
        this.sender = sender;
        this.receiver = receiver;
        this.chatID = uniqueChatID;
        this.uniqueAdID = uniqueAdID;
        this.isActive = isActive;
    }


    /**
     * timeLastMessageSent is used to determine when the last message in a chat was sent. This is to be able to display
     * it for the user to show the relevancy of a message/chat.
     *
     * @return
     */
    @Override
    public long timeLastMessageSent() {

        long timeLastMessageSent = 0;
        for (int i = 0; i < messages.size(); i++) {
            if ((messages.get(i).getTimeSent()) >= (timeLastMessageSent)) {
                timeLastMessageSent = messages.get(i).getTimeSent();

            }
        }
        return timeLastMessageSent;
    }

    public List<iMessage> getMessages() {
        return this.messages;
    }

    public String getSenderID() {
        return this.sender.getId();
    }

    public String getReceiverID() {
        return this.receiver.getId();
    }

    @Override
    public String getChatID() {
        return this.chatID;
    }

    public void setMessages(List<iMessage> messages) {
        this.messages = messages;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }


    @Override
    public String getDisplayName(String currentUserID) {

        if (!(sender.getId().equals(currentUserID))) {
            return sender.getDisplayName();
        } else {
            return receiver.getDisplayName();
        }
    }

    @Override
    public String getAdID() {
        return this.uniqueAdID;

    }
}

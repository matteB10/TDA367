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
    private iUser userOne;
    private iUser userTwo;
    private boolean isActive;
    private String imageURL;


    Chat(String uniqueChatID, iUser userOne, iUser userTwo, String uniqueAdID,String imageURL, boolean isActive) {
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.chatID = uniqueChatID;
        this.uniqueAdID = uniqueAdID;
        this.isActive = isActive;
        this.imageURL = imageURL;
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
    public String getReceiverName(String currentUserID) {

        if (!(userOne.getId().equals(currentUserID))) {
            return userOne.getDisplayName();
        } else {
            return userTwo.getDisplayName();
        }
    }

    @Override
    public String getAdID() {
        return this.uniqueAdID;

    }
    @Override
    public String getImageURL(){
        return this.imageURL;
    }

    @Override
    public String getReceiverID(String currentUserID) {
        if (!(userOne.getId().equals(currentUserID))) {
            return userOne.getId();
        } else {
            return userTwo.getId();
        }
    }
}

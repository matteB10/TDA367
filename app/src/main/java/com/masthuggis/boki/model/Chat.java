package com.masthuggis.boki.model;

import java.util.List;

/**
 * Chat is a java class implementing the iChat interface for the sake of abstraction. Chat is the container of messages which
 * are being sent between two users. The chat contains an advertisementID to make sure that two users can only have one chat considering
 * one specific advertisement.
 * Used by ChatFactory
 * Written by masthuggis
 */

public class Chat implements iChat {
    private List<iMessage> messages;
    private String chatID;
    private String uniqueAdID;
    private String userOneID;
    private String userTwoID;
    private String userOneUsername;
    private String userTwoUsername;
    private boolean isActive;
    private String imageURL;


    Chat(String uniqueChatID, String userOneID, String userTwoID,String userOneUsername,String userTwoUsername, String uniqueAdID,String imageURL, boolean isActive) {
        this.userOneID = userOneID;
        this.userTwoID= userTwoID;
        this.userOneUsername = userOneUsername;
        this.userTwoUsername = userTwoUsername;
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

    /**
     * Used to check which Username to display. Compares an ID from the currentUser to
     * the ids of the users participating in the chat.
     * @param currentUserID
     * @return
     */
    @Override
    public String getReceiverName(String currentUserID) {

        if (!(userOneID.equals(currentUserID))) {
            return userTwoUsername;
        } else {
            return userOneUsername;
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
    /**
     * Used to determine who is the receiver and who is the sender in the
     * chat. This is relative depending which user is currently logged in.
     * Compares an ID from the currentUser to
     * the ids of the users participating in the chat.
     */
    @Override
    public String getReceiverID(String currentUserID) {
        if (!(userOneID.equals(currentUserID))) {
            return userOneID;
        } else {
            return userTwoID;
        }
    }
}

package com.masthuggis.boki.model;

import java.util.List;

/**
 * Chat is a java class implementing the iChat interface for the sake of abstraction. Chat is the container of messages which
 * are being sent between two users. The chat contains an advertisement to make sure that two users can only have one chat considering
 * one specific advertisement.
 */

public class Chat implements iChat {
    private List<iMessage> messages;
    private String senderID;
    private String receiverID;
    private String chatID;
    private String receiverUsername;
    private Advertisement advertisement;
    private String senderUsername;
    private DataModel dataModel;


    public Chat(String uniqueChatID, Advertisement advert, String receiverUsername, String senderUsername,DataModel dataModel) {
        this.dataModel = dataModel;
        this.senderID = dataModel.getUserID();
        this.receiverID = advert.getUniqueOwnerID();
        this.chatID = uniqueChatID;
        this.receiverUsername = receiverUsername;
        this.senderUsername = senderUsername;
        this.advertisement = advert;
        dataModel.getMessages(uniqueChatID, this, messagesList -> messages = messagesList);
    }


    /**
     * timeLastMessageSent is used to determine when the last message in a chat was sent. This is to be able to display
     * it for the user to show the relevancy of a message/chat.
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
        return this.senderID;
    }

    public String getReceiverID() {
        return this.receiverID;
    }

    @Override
    public String getChatID() {
        return this.chatID;
    }

    public void setMessages(List<iMessage> messages) {
        this.messages = messages;
    }


    public String getReceiverUsername() {
        return receiverUsername;
    }

    @Override
    public String getSenderUsername() {
        return this.senderUsername;
    }

    @Override
    public Advertisement getAdvert() {
        return this.advertisement;
    }

    @Override
    public String getDisplayName() {
        String currentUsername = dataModel.getUserDisplayName();

        if (!(senderUsername.equals(currentUsername))) {
            return senderUsername;
        } else {
            return receiverUsername;
        }

    }
}

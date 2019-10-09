package com.masthuggis.boki.model;

import java.util.List;

public class Chat implements iChat {
    private List<iMessage> messages;
    private String senderID;
    private String receiverID;
    private String chatID;
    private String receiverUsername;
    private Advertisement advertisement;
    private String senderUsername;


    public Chat(String uniqueChatID, Advertisement advert, String receiverUsername, String senderUsername) {
        this.senderID = DataModel.getInstance().getUserID();
        this.receiverID = advert.getUniqueOwnerID();
        this.chatID = uniqueChatID;

        // TODO stoppa in receiverusername här istället, blir kaoz annars.
        this.receiverUsername = receiverUsername;
        this.senderUsername = senderUsername;
        this.advertisement = advert;
        DataModel.getInstance().getMessages(uniqueChatID, this, messagesList -> messages = messagesList);
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
    public long timeLastMessageSent() {

        long timeLastMessageSent = 0;
        for (int i = 0; i < messages.size(); i++) {
            if ((messages.get(i).getTimeSent()) >= (timeLastMessageSent)) {
                timeLastMessageSent = messages.get(i).getTimeSent();

            }
        }
        return timeLastMessageSent;
    }

    @Override
    public Advertisement getAdvert() {
        return this.advertisement;
    }

    @Override
    public String getDisplayName() {
        String currentUsername = DataModel.getInstance().getUserDisplayName();

        if (!(senderUsername.equals(currentUsername))) {
            return senderUsername;
        } else {
            return receiverUsername;
        }
    }
}

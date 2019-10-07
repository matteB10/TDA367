package com.masthuggis.boki.model;

import java.util.List;

public class Chat implements iChat {
    private List<iMessage> messages;
    private String sender;
    private String receiver;
    private String chatID;
    private String receiverUsername;
    private Advertisement advertisement;


    public Chat(String uniqueChatID,Advertisement advert) {
        this.sender = DataModel.getInstance().getUserID();
        this.receiver = advert.getUniqueOwnerID();
        this.chatID = uniqueChatID;
        this.receiverUsername = advert.getOwner();
        this.advertisement = advert;
        DataModel.getInstance().getMessages(uniqueChatID, this, messagesList -> messages = messagesList);
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
}

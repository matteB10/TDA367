package com.masthuggis.boki.model;

import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.backend.messagesCallback;

import java.util.ArrayList;
import java.util.List;

public class Chat implements iChat {
    private List<iMessage> messages;
    private List<ChatObservers> chatObserversList = new ArrayList<>();
    private String sender;
    private String receiver;
    private String chatID;
    private String receiverUsername;


    public Chat(String sender, String receiver, String uniqueChatID, String receiverUsername) {
        this.sender = sender;
        this.receiver = receiver;
        this.chatID = uniqueChatID;
        this.receiverUsername = receiverUsername;
        UserRepository.getInstance().getMessages(uniqueChatID, this, new messagesCallback() {

            @Override
            public void onCallback(List<iMessage> messagesList) {
                messages = messagesList;
                updateChatObservers();

            }
        });
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

    public void addChatObserver(ChatObservers chatObserver) {
        this.chatObserversList.add(chatObserver);
    }

    public void removeChatObserver(ChatObservers chatObserver) {
        this.chatObserversList.remove(chatObserver);
    }

    public void updateChatObservers() {
        if (chatObserversList.size() == 0) {
            return;
        }
        for (ChatObservers chatObservers : chatObserversList) {
            chatObservers.onChatUpdated();
        }
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    @Override
    public String timeLastMessageSent() {
        String timeLastMessageSent = "0";
        for (int i = 0; i < messages.size(); i++) {
            if (Integer.parseInt(messages.get(i).getTimeSent()) >= Integer.parseInt(timeLastMessageSent)) {
                timeLastMessageSent = messages.get(i).getTimeSent();

            }
        }
        return timeLastMessageSent;
    }
}

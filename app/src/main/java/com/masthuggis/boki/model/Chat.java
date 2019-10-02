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


    public Chat(String sender, String receiver, String uniqueChatID) {
        this.sender = sender;
        this.receiver = receiver;
        this.chatID = uniqueChatID;
        UserRepository.getInstance().getMessages(uniqueChatID, new messagesCallback() {

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

    public void addChatObserver( ChatObservers chatObserver){
        this.chatObserversList.add(chatObserver);
    }
    public void removeChatObserver(ChatObservers chatObserver){
        this.chatObserversList.remove(chatObserver);
    }
    private void updateChatObservers(){
        if(chatObserversList.size()==0){
            return;
        }
        for(ChatObservers chatObservers :chatObserversList){
            chatObservers.onChatUpdated();
        }
    }

}

package com.masthuggis.boki.model;

/**
 *
 *Used by Chat,iChat,Message, MessageFactory,messagesCallback,
 * MessagesPresenter, UserRepository
 * Written by masthuggis
 */
public interface iMessage {
    String getMessage();
    long getTimeSent();
    String getSenderID();
}

package com.masthuggis.boki.model;

import java.util.List;

/**
 *
 * Used by Chat, chatCallback, ChatFactory, ChatPresenter,
 * DataModel, iUser,MessagesPresenter, User, UserRepository
 * Written by masthuggis
 */
public interface iChat {
    List<iMessage> getMessages();

    String getChatID();

    long timeLastMessageSent();

    String getReceiverName(String currentUserID);

    String getAdID();

    void setMessages(List<iMessage> messagesList);

    boolean isActive();

    String getImageURL();

    String getReceiverID(String currentUserID);
}

package com.masthuggis.boki.view;
/**
 * Interface that represents a chat thumbnail
 * Used by ChatPresenter and MessagesRecyclerViewAdapter.
 * Written by masthuggis
 */
public interface ChatThumbnailView {
     void setUserTextView(String userTextView);

    void setDateTextView(String dateTextView);

    void setMessageImageView(String messageImageView);

    void setChatID(String chatID);

    void hide();

    void show();
}

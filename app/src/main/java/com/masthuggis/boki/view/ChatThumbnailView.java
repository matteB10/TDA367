package com.masthuggis.boki.view;
/**
 *
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

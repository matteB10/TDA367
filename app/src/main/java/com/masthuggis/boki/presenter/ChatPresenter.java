package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.ChatObserver;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.view.MessagesRecyclerViewAdapter;

import java.util.List;

public class ChatPresenter implements ChatObserver {

    private List<iChat> chats;
    private View view;

    public ChatPresenter(View view) {
        this.view = view;
        this.view.showLoadingScreen();
        if (DataModel.getInstance().isLoggedIn()) {
            chats = DataModel.getInstance().getUserChats();
            view.isLoggedIn(this);

        }
        this.view.hideLoadingScreen();


        DataModel.getInstance().addChatObserver(this);
        // UserRepository.getInstance().getUserChats(DataModel.getInstance().getUserID(), chatList -> chats = chatList);

    }

    public void onRowPressed(String chatID) {
        view.showDetailsScreen(chatID);
    }

    public void bindViewHolderAtPosition(int position, MessagesRecyclerViewAdapter.
            ViewHolder holder) {
        if (chats.size() < position || chats == null) {
            return;
        }
        iChat c = chats.get(position);

        holder.setUserTextView(c.getReceiverUsername());
        holder.setChatID(c.getChatID());
        holder.setDateTextView("" + c.timeLastMessageSent());
        holder.setMessageImageView(c.getAdvert().getImageUrl());

    }

    public int getItemCount() {
        if (chats != null) {
            return chats.size();
        }
        return 0;
    }


    @Override
    public void onChatUpdated() {
        this.chats = DataModel.getInstance().getUserChats();

    }

    public void onDestroy() {
        DataModel.getInstance().removeChatObserver(this);
    }


    public interface View {
        void showLoadingScreen();

        void showThumbnails(ChatPresenter chatPresenter);

        void hideLoadingScreen();

        void showDetailsScreen(String chatID);

        void isLoggedIn(ChatPresenter chatPresenter);
    }
}
package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.view.MessagesRecyclerViewAdapter;

import java.util.List;

public class MessagesPresenter {

    private List<iChat> chats;
    private View view;

    public MessagesPresenter(View view) {
        this.view = view;
        this.view.showLoadingScreen();
        if (DataModel.getInstance().isLoggedIn()) {
            chats= DataModel.getInstance().getUserChats();
            view.isLoggedIn( this);
        }



            // UserRepository.getInstance().getUserChats(DataModel.getInstance().getUserID(), chatList -> chats = chatList);

    }
    public void onRowPressed(String userID) {
        view.showDetailsScreen(userID);
    }
    public void bindViewHolderAtPosition(int position, MessagesRecyclerViewAdapter.
            ViewHolder holder) {
        if (chats.size() < position || chats == null) {
            return;
        }
        iChat c = chats.get(position);
        holder.setUserTextView(c.getReceiver());

    }

    public int getItemCount() {
        if (chats != null) {
            return chats.size();
        }
        return 0;
    }


    public interface View {
        void showLoadingScreen();

        void showThumbnails(MessagesPresenter messagesPresenter);

        void hideLoadingScreen();

        void showDetailsScreen(String id);

        void isLoggedIn(MessagesPresenter messagesPresenter);
    }
}
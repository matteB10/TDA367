package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.utils.ClickDelayHelper;
import com.masthuggis.boki.view.MessagesRecyclerViewAdapter;

import java.util.List;

/**
 * ChatPresenter is the presenter class for the view called ChatFragment.
 */

public class ChatPresenter implements ChatObserver {

    private List<iChat> chats;
    private View view;
    private DataModel dataModel;


    public ChatPresenter(View view, DataModel dataModel) {
        this.view = view;
        this.dataModel = dataModel;
        this.view.showLoadingScreen();
        chats = this.dataModel.getUserChats();
        view.showUserChats(this);
        this.view.hideLoadingScreen();
        for (iChat chat : chats) {
            if (!(chat.isActive())) {
                view.displayToast(chat.getReceiverName(dataModel.getUserID()));
                dataModel.removeChat(dataModel.getUserID(), chat.getChatID());

            }
        }
        this.chats = this.dataModel.getUserChats();
        this.dataModel.addChatObserver(this);
    }

    /**
     * Depending on which position of the recyclerview is pressed, different messagesScreens are displayed.
     */

    public void onRowPressed(String chatID) {

        for (iChat chat : chats) {
            if (chat.getChatID().equals(chatID)) {
                if (chat.isActive()) {
                    return;
                }
            }
        }
        view.showMessagesScreen(chatID);
    }

    public void bindViewHolderAtPosition(int position, MessagesRecyclerViewAdapter.
            ViewHolder holder) {
        if (chats.size() < position || chats == null) {
            return;
        }
        iChat c = chats.get(position);

        //TODO FIXA SÅ ATT TIDEN SYNS HÄR MED ETT VETTIGT DATUMSYSTEM
        //  String timeLastMessageSent = c.timeLastMessageSent();
        holder.setUserTextView(c.getReceiverName(dataModel.getUserID()));
        holder.setChatID(c.getChatID());
        holder.setDateTextView("" + c.timeLastMessageSent());
        holder.setMessageImageView(c.getImageURL());
        view.disableClickOnChat();

    }

    /**
     * Used by the recyclerview to decide how many items to display in the view.
     */

    public int getItemCount() {
        if (chats != null) {
            return chats.size();
        }
        return 0;
    }

    /**
     * Updates chat view when the model is updated.
     */
    @Override
    public void onChatUpdated() {
        this.chats = dataModel.getUserChats();

    }

    public void onDestroy() {
        dataModel.removeChatObserver(this);
    }

    /**
     * Makes sure the user cant open multiple chats at the same time by tapping a chat multiple times in a row.
     */
    public boolean canProceedWithTapAction() {
        return ClickDelayHelper.canProceedWithTapAction();
    }


    public interface View {
        void showLoadingScreen();

        void showThumbnails(ChatPresenter chatPresenter);

        void hideLoadingScreen();

        void showMessagesScreen(String chatID);

        void showUserChats(ChatPresenter chatPresenter);

        void displayToast(String displayName);

        void disableClickOnChat();
    }
}
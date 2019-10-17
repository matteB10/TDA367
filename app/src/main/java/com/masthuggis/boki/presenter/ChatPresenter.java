package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.utils.ClickDelayHelper;
import com.masthuggis.boki.view.ChatThumbnailView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        checkIfChatsAreActive();
        this.chats = this.dataModel.getUserChats();
        sortChatsAccordingToLastMessageSent();
        this.dataModel.addChatObserver(this);
    }

    private void checkIfChatsAreActive() {
        for (iChat chat : chats) {
            if (!(chat.isActive())) {
                view.displayToast(chat.getReceiverName(dataModel.getUserID()));
                dataModel.removeChat(dataModel.getUserID(), chat.getChatID());

            }
        }
    }

    /**
     * Depending on which position of the recyclerview is pressed, different messagesScreens are displayed.
     */

    public void onRowPressed(String chatID) {

        for (iChat chat : chats) {
            if (chat.getChatID().equals(chatID)) {
                view.showMessagesScreen(chatID);
                return;

            }
        }
    }

    public void bindViewHolderAtPosition(int position, ChatThumbnailView holder) {
        if (chats == null ||chats.size() < position) {
            return;
        }
        iChat c = chats.get(position);
        String str = formatTimeLastMessageSent(c.timeLastMessageSent());
        holder.setDateTextView(str);
        holder.setUserTextView(c.getReceiverName(dataModel.getUserID()));
        holder.setChatID(c.getChatID());
        holder.setMessageImageView(c.getImageURL());
    }

    private String formatTimeLastMessageSent(long l) {
        if(l ==0){
            return "";
        }
        String str = Long.toString(l);
        if(str.length()> 6){
            return "Invalid time format.";
        }
        char[] cArr = str.toCharArray();
        String st = "" + cArr[6] + cArr[7] + "." + cArr[8] + cArr[9] + "." + cArr[10] + cArr[11]
                + " " + cArr[4] + cArr[5] + "/" + cArr[2] + cArr[3] + " - " + cArr[0] + cArr[1];
        return st;
    }

    private void sortChatsAccordingToLastMessageSent() {
        List<iChat> sorted = new ArrayList<>(chats).stream()
                .sorted((adOne, adTwo) -> ((int) (adOne.timeLastMessageSent() - adTwo.timeLastMessageSent())))
                .collect(Collectors.toList());
        Collections.reverse(sorted);

        chats = sorted;
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
        sortChatsAccordingToLastMessageSent();
        view.updateThumbnails();


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
        void updateThumbnails();

    }
}
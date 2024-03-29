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
 * Used by ChatFragment and MessagesRecyclerViewAdapter
 * Written by masthuggis
 */
public class ChatPresenter<T extends ListPresenterView & ChatPresenter.View> extends ListPresenter<iChat, ChatThumbnailView> implements ChatObserver {

    private final T view;

    public ChatPresenter(T view, DataModel dataModel) {
        super(view, dataModel);
        this.view = view;
        this.dataModel.addChatObserver(this);
    }

    @Override
    public List<iChat> getData() {
        return super.dataModel.getUserChats();
    }

    @Override
    public List<iChat> sort(List<iChat> data) {
        return sortChatsAccordingToLastMessageSent(data);
    }

    /**
     * Binds the thumbnail containing information about a specific chat to the
     * RecyclerView containing the thumbnail.
     * @param position The zero-indexed position where thumbnail will be bound.
     * @param dataView The thumbnail to be bound.
     */
    @Override
    public void onBindThumbnailViewAtPosition(int position, ChatThumbnailView dataView) {
        List<iChat> chats = getCurrentDisplayedData();
        if (chats == null || chats.size() < position || chats.size() == 0) {
            return;
        }
        iChat c = chats.get(position);
        String str = formatTimeLastMessageSent(c.timeLastMessageSent());
        dataView.setDateTextView(str);
        dataView.setUserTextView(c.getReceiverName(dataModel.getUserID()));
        dataView.setChatID(c.getChatID());
        dataView.setMessageImageView(c.getImageURL());
        hideChatsWithoutMessages(c, dataView);
    }

    private void checkIfChatsAreActive() {
        for (iChat chat : getCurrentDisplayedData()) {
            if (!(chat.isActive())) {
                view.displayToast(chat.getReceiverName(dataModel.getUserID()));
                dataModel.removeChat(dataModel.getUserID(), chat.getChatID());
            }
        }
    }
    /**
     * Hides all of the user's chats containing no messages.
     * Necessary to implement because chats without messages can be created,
     * but these should never be shown to the user.
     */
    private void hideChatsWithoutMessages(iChat chat,ChatThumbnailView dataView){
        if(chat.getMessages()== null || chat.getMessages().size()<=0){
            dataView.hide();
        }else{
            dataView.show();
        }
    }

    /**
     * Depending on which position of the recyclerview is pressed, different messagesScreens are displayed.
     */
    @Override
    public void onRowPressed(String chatID) {
        for (iChat chat : getCurrentDisplayedData()) {
            if (chat.getChatID().equals(chatID)) {
                view.showMessagesScreen(chatID);
                return;
            }
        }
    }

    /**
     * Used to format the time of the last message sent, displays both time of day and date.
     * Format used is hh.mm.ss dd/mm - yy.
     */
    private String formatTimeLastMessageSent(long l) {
        if (l == 0) {
            return "";
        }
        String str = Long.toString(l);
        if (str.length() != 12) {
            return "Invalid time format.";
        }
        char[] cArr = str.toCharArray();
        String st = "" + cArr[6] + cArr[7] + "." + cArr[8] + cArr[9] + "." + cArr[10] + cArr[11]
                + " " + cArr[4] + cArr[5] + "/" + cArr[2] + cArr[3] + " - " + cArr[0] + cArr[1];
        return st;
    }

    /**
     * @param chats The list containing the chats to be sorted
     * @return A sorted list of chats, sorted with the chat with the most recent message first
     */
    private List<iChat> sortChatsAccordingToLastMessageSent(List<iChat> chats) {
        if (chats == null) {
            return new ArrayList<>();
        }
        List<iChat> sorted = new ArrayList<>(chats).stream()
                .sorted((adOne, adTwo) -> ((int) (adOne.timeLastMessageSent() - adTwo.timeLastMessageSent())))
                .collect(Collectors.toList());
        Collections.reverse(sorted);
        return sorted;
    }

    /**
     * Updates chat view when the chats in the model are updated.
     */
    @Override
    public void onChatUpdated() {
        super.updateData();
        checkIfChatsAreActive();
        if(getData().size()<=0){
            view.hideRecyclerView();
        }
    }

    public void onDestroy() {
        dataModel.removeChatObserver(this);
    }

    /**
     * Makes sure the user cant open multiple chats at the same time by
     * tapping the same chat multiple times in a row.
     */
    public boolean canProceedWithTapAction() {
        return ClickDelayHelper.canProceedWithTapAction();
    }

    public interface View {
        void showMessagesScreen(String chatID);

        void displayToast(String displayName);
        void hideRecyclerView();
    }
}
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

public class ChatPresenter<T extends ListPresenterView & ChatPresenter.View> extends AdvertsPresenter<iChat, ChatThumbnailView> implements ChatObserver {

    private final T view;

    public ChatPresenter(T view, DataModel dataModel) {
        super(view, dataModel);
        this.view = view;
        checkIfChatsAreActive();
        this.dataModel.addChatObserver(this);
    }

    @Override
    public List<iChat> getData() {
        return super.dataModel.getUserChats();
    }

    @Override
    public List<iChat> sort(List<iChat> adverts) {
        return sortChatsAccordingToLastMessageSent(adverts);
    }

    @Override
    public void onBindThumbnailViewAtPosition(int position, ChatThumbnailView thumbnailView) {
        List<iChat> chats = getCurrentDisplayedAds();
        if (chats == null || chats.size() < position) {
            return;
        }
        iChat c = chats.get(position);
        String str = formatTimeLastMessageSent(c.timeLastMessageSent());
        thumbnailView.setDateTextView(str);
        thumbnailView.setUserTextView(c.getReceiverName(dataModel.getUserID()));
        thumbnailView.setChatID(c.getChatID());
        thumbnailView.setMessageImageView(c.getImageURL());
    }

    private void checkIfChatsAreActive() {
        for (iChat chat : getCurrentDisplayedAds()) {
            if (!(chat.isActive())) {
                view.displayToast(chat.getReceiverName(dataModel.getUserID()));
                dataModel.removeChat(dataModel.getUserID(), chat.getChatID());
            }
        }
    }

    /**
     * Depending on which position of the recyclerview is pressed, different messagesScreens are displayed.
     */
    @Override
    public void onRowPressed(String chatID) {
        for (iChat chat : getCurrentDisplayedAds()) {
            if (chat.getChatID().equals(chatID)) {
                view.showMessagesScreen(chatID);
                return;
            }
        }
    }

    private String formatTimeLastMessageSent(long l) {
        if(l ==0){
            return "";
        }
        String str = Long.toString(l);
        if(str.length()> 12){
            return "Invalid time format.";
        }
        char[] cArr = str.toCharArray();
        String st = "" + cArr[6] + cArr[7] + "." + cArr[8] + cArr[9] + "." + cArr[10] + cArr[11]
                + " " + cArr[4] + cArr[5] + "/" + cArr[2] + cArr[3] + " - " + cArr[0] + cArr[1];
        return st;
    }

    private List<iChat> sortChatsAccordingToLastMessageSent(List<iChat> chats) {
        List<iChat> sorted = new ArrayList<>(chats).stream()
                .sorted((adOne, adTwo) -> ((int) (adOne.timeLastMessageSent() - adTwo.timeLastMessageSent())))
                .collect(Collectors.toList());
        Collections.reverse(sorted);
        return sorted;
    }

    /**
     * Updates chat view when the model is updated.
     */
    @Override
    public void onChatUpdated() {
        super.updateAdverts();
        /*
        this.chats = dataModel.getUserChats();
        sortChatsAccordingToLastMessageSent();
        view.updateThumbnails();
         */
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
        void showMessagesScreen(String chatID);

        void displayToast(String displayName);
    }
}
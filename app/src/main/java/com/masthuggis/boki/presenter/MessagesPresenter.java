package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.MessagesObserver;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.model.iMessage;
import com.masthuggis.boki.utils.CurrentTimeHelper;

import java.util.HashMap;
import java.util.List;

public class MessagesPresenter implements MessagesObserver {
    private View view;
    private iChat chat;
    private List<iMessage> messages;
    private String chatID;

    public MessagesPresenter(View view, String chatID) {
        this.view = view;
        this.chatID = chatID;
        DataModel.getInstance().addMessagesObserver(this);
        chat = DataModel.getInstance().findChatByID(chatID);
        if (chat != null) {
            messages = chat.getMessages();
            if (messages != null || messages.size() > 0) {
                populateView(messages);
            }
        }


    }

    private void populateView(List<iMessage> messages) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            setMessageBox(messages.get(i).getMessage(), messages.get(i).getSenderID().equals(DataModel.getInstance().getUserID()));
        }
        /*for (iMessage message : messages) {
            setMessageBox(message.getMessage(), message.getSenderID().equals(DataModel.getInstance().getUserID()));
        }*/
    }


    public void messageActivityStarted() {

        //TODO KOLLA INLOGGAD ANVÄNDARE OSV. SÄTT BILD, ÖPPNA CHATTAR YADA YADA.....

    }

    public void sendMessage(String messageText) {

        HashMap<String, Object> map = new HashMap<>();
        if (!messageText.equals("")) {
            //TODO FIX TIME OF MESSAGE SENT, MAKE SURE TO NOT OVEREXTEND THE MAXIMUM VALUE OF INTEGER
            //Date, message, sender
            map.put("message", messageText);
            map.put("sender", DataModel.getInstance().getUserID());
            map.put("timeSent", CurrentTimeHelper.getCurrentTimeNumerical());
            DataModel.getInstance().sendMessage(chat.getChatID(), map);

            setMessageBox(messageText, true);

            //behövs nog ej.
           // onChatUpdated();

        }
    }

    private void setMessageBox(String messageText, boolean sentByCurrentUser) {
        view.addMessageBox(messageText, sentByCurrentUser);
    }

    private void onChatUpdated() {
        if (DataModel.getInstance().findChatByID(chatID) == null) {
        } else {
            chat = DataModel.getInstance().findChatByID(chatID);
            view.update();
            messages = chat.getMessages();
            populateView(messages);
        }
    }

    public void onDestroy() {
        DataModel.getInstance().removeMessagesObserver(this);
    }

    @Override
    public void onMessagesChanged() {
        onChatUpdated();
    }

    public interface View {

        void addMessageBox(String messageText, boolean sentByCurrentUser);

        void update();
    }
}

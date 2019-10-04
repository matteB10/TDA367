package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.ChatObservers;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.model.iMessage;
import com.masthuggis.boki.utils.CurrentTimeHelper;

import java.util.HashMap;
import java.util.List;

public class ChatPresenter implements ChatObservers {
    private View view;
    private iChat chat;
    private List<iMessage> messages;

    public ChatPresenter(View view, String chatID) {
        this.view = view;
        chat = DataModel.getInstance().findChatByID(chatID);
        chat.addChatObserver(this);

        messages = chat.getMessages();
        populateView(messages);


    }

    private void populateView(List<iMessage> messages) {
        for(int i=messages.size()-1;i>=0;i--){
            setMessageBox(messages.get(i).getMessage(),messages.get(i).getSenderID().equals(DataModel.getInstance().getUserID()));
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

            //Date, message, sender
            map.put("message", messageText);
            map.put("sender", DataModel.getInstance().getUserID());
            map.put("timeSent", CurrentTimeHelper.getCurrentTimeNumerical());
            DataModel.getInstance().sendMessage(chat.getChatID(), map);

            setMessageBox(messageText, true);
            onChatUpdated();

        }
    }

    private void setMessageBox(String messageText, boolean sentByCurrentUser) {
        view.addMessageBox(messageText, sentByCurrentUser);
    }

    @Override
    public void onChatUpdated() {
        view.update();
        messages = chat.getMessages();
        populateView(messages);


    }

    public void onDestroy() {
        chat.removeChatObserver(this);
    }

    public interface View {

        void addMessageBox(String messageText, boolean sentByCurrentUser);

        void update();
    }
}

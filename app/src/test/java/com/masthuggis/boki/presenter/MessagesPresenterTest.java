package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.ChatFactory;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.view.MessagesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MessagesPresenterTest {
    @Mock
    private MessagesActivity activityMock;
    @Mock
    private DataModel databaseMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private MessagesPresenter presenter;
    private int numMessagesToAdd = 0;
    private String chatID = "chatID";
    private iChat chat = ChatFactory.createChat(chatID, ChatMessagesHelper.generate10Users().get(0), ChatMessagesHelper.generate10Users().get(1), "uniqueAdID1", "123", true);

    private void setup() {
        Mockito.when(databaseMock.findChatByID(chatID)).thenReturn(chat);
        List<iChat> chats = new ArrayList<>();
        chats.add(chat);
        ChatMessagesHelper.addMessagesToChats(chats, numMessagesToAdd);
    }

    @Test
    public void whenLoadedMessagesIsShownWhenMessagesExists() {
        numMessagesToAdd = 10;
        setup();

        presenter = new MessagesPresenter(activityMock, chatID, databaseMock);

        Mockito.verify(activityMock, times(numMessagesToAdd)).addMessageBox(anyString(), anyBoolean());
    }

    @Test
    public void whenLoadedMessagesIsNotShownWhenMessagesNotExists() {
        numMessagesToAdd = 0;
        setup();

        presenter = new MessagesPresenter(activityMock, chatID, databaseMock);

        Mockito.verify(activityMock, times(numMessagesToAdd)).addMessageBox(anyString(), anyBoolean());
    }

    @Test
    public void newMessageIsShownWhenValidMessageIsSent() {
        numMessagesToAdd = 10;
        setup();

        presenter = new MessagesPresenter(activityMock, chatID, databaseMock);
        presenter.sendMessage("Valid message text");

        Mockito.verify(activityMock, times(numMessagesToAdd + 1)).addMessageBox(anyString(), anyBoolean());
        Mockito.verify(databaseMock).sendMessage(anyString(), any());
    }

    @Test
    public void newMessageIsNotShownWhenInValidMessageIsSent() {
        numMessagesToAdd = 10;
        setup();

        presenter = new MessagesPresenter(activityMock, chatID, databaseMock);
        presenter.sendMessage("");

        Mockito.verify(activityMock, times(numMessagesToAdd)).addMessageBox(anyString(), anyBoolean());
        Mockito.verify(databaseMock, times(0)).sendMessage(anyString(), any());
    }

    @Test
    public void viewIsUpdatedWhenUpdateHappened() {
        numMessagesToAdd = 30;
        setup();

        presenter = new MessagesPresenter(activityMock, chatID, databaseMock);
        presenter.onMessagesChanged();

        Mockito.verify(activityMock).update();
    }

}

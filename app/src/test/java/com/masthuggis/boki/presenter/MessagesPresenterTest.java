package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.ChatFactory;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.utils.UniqueIdCreator;
import com.masthuggis.boki.view.ListView;
import com.masthuggis.boki.view.MessagesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String chatID = "chatID";
    private iChat chat = ChatFactory.createChat(chatID, ChatMessagesHelper.generate10Users().get(0), ChatMessagesHelper.generate10Users().get(1), "uniqueAdID1", "123", true);

    private void setup(int numMessagesToAdd) {
        Mockito.when(databaseMock.findChatByID(chatID)).thenReturn(chat);
        List<iChat> chats = new ArrayList<>();
        chats.add(chat);
        ChatMessagesHelper.addMessagesToChats(chats, numMessagesToAdd);
    }

    @Test
    public void whenLoadedMessagesIsShownWhenMessagesExists() {
        setup(10);

        presenter = new MessagesPresenter(activityMock, chatID, databaseMock);

        Mockito.verify(activityMock, times(10)).addMessageBox(anyString(), anyBoolean());
    }

    @Test
    public void whenLoadedMessagesIsNotShownWhenMessagesNotExists() {
        setup(0);

        presenter = new MessagesPresenter(activityMock, chatID, databaseMock);

        Mockito.verify(activityMock, times(0)).addMessageBox(anyString(), anyBoolean());
    }

    
}

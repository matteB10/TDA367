package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.ChatFactory;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.MessageFactory;
import com.masthuggis.boki.model.UserFactory;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.model.iUser;
import com.masthuggis.boki.utils.UniqueIdCreator;
import com.masthuggis.boki.view.ChatFragment;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChatPresenterTest {

    @Mock
    private DataModel databaseMock;
    @Mock
    private ChatFragment chatFragmentMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private List<iUser> generate10Users() {
        List<iUser> tenUsers = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            tenUsers.add(UserFactory.createUser("mail" + i + "@mail.se", "displayname" + i, "userid" + i));
        }
        return tenUsers;

    }

    private List<iChat> userChats() {
        List<iUser> tenUsers = generate10Users();
        List<iChat> userChats = new ArrayList<>();
        boolean isActive;
        for (int i = 0; i < 5; i++) {
            isActive = i % 2 == 0;
            iChat chat = ChatFactory.createChat(UniqueIdCreator.getUniqueID(), tenUsers.get(i), tenUsers.get(i + 1), "uniqueAdID1", "123", isActive);
            userChats.add(chat);
        }
        return userChats;
    }

    private List<iChat> removeInactiveChats(List<iChat> list) {
        List<iChat> activeChats = new ArrayList<>();
        for (iChat chat : list) {
            if ((chat.isActive())) {
                activeChats.add(chat);

            }
        }
        return activeChats;

    }
    private void addMessagesToChats(List<iChat> chats){
        // hhMMss DDmmYY
        String str= "191016133503";
        long l = Long.str
        for (iChat chat:chats){
            chat.getMessages().add(MessageFactory.createMessage("meddelande",l,"jag"));
        }
    }

    @Test
   public void onCreateTest() {
        List<iChat> userChats = userChats();
        Mockito.doNothing().when(chatFragmentMock).showUserChats(null);
        Mockito.doNothing().when(chatFragmentMock).hideLoadingScreen();
        Mockito.when(databaseMock.getUserChats()).thenReturn(userChats);


        Mockito.doNothing().when(databaseMock).removeChat("", "");
        userChats = removeInactiveChats(userChats);
        Mockito.doNothing().when(databaseMock).addChatObserver(null);


        ChatPresenter presenter = new ChatPresenter(chatFragmentMock, databaseMock);

        assert(userChats.size()==5);
        verify(chatFragmentMock, times(5)).displayToast("");
    }

}

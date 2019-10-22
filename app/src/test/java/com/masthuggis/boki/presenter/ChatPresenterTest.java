package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.ChatFactory;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.MessageFactory;
import com.masthuggis.boki.model.UserFactory;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.model.iMessage;
import com.masthuggis.boki.model.iUser;
import com.masthuggis.boki.utils.UniqueIdCreator;
import com.masthuggis.boki.view.ChatFragment;
import com.masthuggis.boki.view.ChatThumbnailView;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static com.masthuggis.boki.presenter.ChatMessagesHelper.addMessagesToChats;
import static com.masthuggis.boki.presenter.ChatMessagesHelper.generate10Users;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChatPresenterTest {

    @Mock
    private DataModel databaseMock;
    @Mock
    private ChatFragment chatFragmentMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    ChatThumbnailView holder;
    @Mock
    ChatPresenter mockPresenter;
    @Mock
    ListPresenter mockListPresenter;

    private List<iChat> userChats = userChats();

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

    private void removeInactiveChatsFromUserChats() {
        List<iChat> newChatList = new ArrayList<>();
        for (iChat chat : userChats) {
            if ((chat.isActive())) {
                newChatList.add(chat);

            }
        }
        userChats = newChatList;
    }

    @Test
    public void onCreateTest() {
        userChats = userChats();
        initMockPresenter();

        initMockRemoveChats();
        ChatPresenter presenter = new ChatPresenter(chatFragmentMock, databaseMock);

        presenter.onChatUpdated();

        assert (userChats.size() == 3);
        verify(chatFragmentMock, times(1)).displayToast("displayname1");
        verify(chatFragmentMock, times(1)).displayToast("displayname3");
        assert (userChats.get(0).getMessages().get(0).getTimeSent() < userChats.get(0).getMessages().get(1).getTimeSent());

    }

    private void initMockPresenter() {
        Mockito.doNothing().when(chatFragmentMock).hideLoadingScreen();
        Mockito.when(databaseMock.getUserChats()).thenReturn(userChats);
        addMessagesToChats(userChats, 5);
        when(databaseMock.getUserID()).thenReturn("");
    }


    private void initMockRemoveChats() {
        for (int i = 0; i < userChats.size(); i++) {
            Mockito.doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    removeInactiveChatsFromUserChats();
                    return null;
                }
            }).when(databaseMock).removeChat("", userChats.get(i).getChatID());
        }
    }

    @Test
    public void onFormatTimeLastMessageSent() {
        initMockPresenter();
        List<iUser> users = generate10Users();
        List<iChat> chatWithBadFormatting = new ArrayList<>();
        chatWithBadFormatting.add(ChatFactory.createChat("chatid",users.get(0),users.get(1),"adid","url",true));
        List<iMessage> messageListWithBadNumberFormat = new ArrayList<>();
        messageListWithBadNumberFormat.add(MessageFactory.createMessage("message", 123, "senderID"));
        chatWithBadFormatting.get(0).setMessages(messageListWithBadNumberFormat);


        when(databaseMock.getUserChats()).thenReturn(chatWithBadFormatting);
        ChatPresenter presenter = new ChatPresenter(chatFragmentMock, databaseMock);
        mockPresenter.onBindThumbnailViewAtPosition(0, holder);
        presenter.updateData();
        presenter.onBindThumbnailViewAtPosition(0, holder);
        verify(holder, times(1)).setDateTextView("Invalid time format.");

        messageListWithBadNumberFormat.clear();
        iMessage messageWithGoodFormat = MessageFactory.createMessage("Message",191021120159L,"senderID");
        messageListWithBadNumberFormat.add(messageWithGoodFormat);
        presenter.onBindThumbnailViewAtPosition(0,holder);
        verify(holder,times(1)).setDateTextView("12.01.59 21/10 - 19");
    }

    @Test
    public void whenChatPressedMessagesShouldBeOpenedForValidChatID() {
        String validID = "validID";
        List<iUser> tenUsers = generate10Users();
        List<iChat> chat = new ArrayList<>();
        chat.add(ChatFactory.createChat(validID, tenUsers.get(0), tenUsers.get(1), "", "123", true));
        Mockito.when(databaseMock.getUserChats()).thenReturn(chat);
        ChatPresenter presenter = new ChatPresenter(chatFragmentMock, databaseMock);

        presenter.updateData();
        presenter.onRowPressed(validID);

        verify(chatFragmentMock).showMessagesScreen(validID);
    }

    @Test
    public void whenChatPressedMessagesShouldNotBeOpenedForInValidChatID() {
        String invalidID = "validID";
        List<iUser> tenUsers = generate10Users();
        List<iChat> chat = new ArrayList<>();
        chat.add(ChatFactory.createChat(invalidID, tenUsers.get(0), tenUsers.get(1), "", "123", true));
        Mockito.when(databaseMock.getUserChats()).thenReturn(chat);
        ChatPresenter presenter = new ChatPresenter(chatFragmentMock, databaseMock);

        presenter.updateData();
        presenter.onRowPressed(invalidID);

        verify(chatFragmentMock).showMessagesScreen(invalidID);
    }

}

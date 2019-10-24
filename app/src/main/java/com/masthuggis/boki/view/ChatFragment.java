package com.masthuggis.boki.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.ChatPresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;
import com.masthuggis.boki.utils.ViewCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying active chats in a list of the current user. Starts
 * MessagesActivity if a chat is pressed.
 * Used by MainActivity.
 * Written by masthuggis.
 */
public class ChatFragment extends ListView implements ChatPresenter.View {
    private ChatPresenter<ChatFragment> presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.presenter = new ChatPresenter<>(this, DependencyInjector.injectDataModel());
        View v = super.onCreateView(inflater, container, savedInstanceState);
        setupRecyclerView();
        presenter.updateData();
        return v;
    }

    /**
     * Initializes the RecyclerView which makes out the visual column holding the Chat-objects
     * The layout of the objects held in this RecyclerView is defined by the GridSpacingItemDecoration
     */
    private void setupRecyclerView() {
        List<RecyclerView.ItemDecoration> decorations = new ArrayList<>();
        decorations.add(new GridSpacingItemDecoration(1, 25, true));
        setItemDecorations(decorations);
        setLayoutManager(new GridLayoutManager(getContext(), 1));
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MessagesRecyclerViewAdapter(getContext(), presenter);
    }

    @Override
    protected View onCreateHeaderLayout() {
        return ViewCreator.createHeader(getContext(), getString(R.string.myMessages));
    }

    @Override
    protected View onCreateNoResultsFoundLayout() {
        return ViewCreator.createSimpleText(getContext(), getString(R.string.noChatsFound));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    /**
     * Displays the messages screen of the chosen chat.
     */
    @Override
    public void showMessagesScreen(String chatID) {
        Intent intent = new Intent(getContext(), MessagesActivity.class);
        intent.putExtra("chatID",chatID);
        startActivity(intent);
    }

    /**
     * Displays a pop-up message called a Toast containing a message that the
     * advertisement associated with the Chat in question has been deleted.
     * @param displayName A String containing the name of the other user participating in the Chat
     */
    @Override
    public void displayToast(String displayName) {
        Context context = getContext();
        CharSequence text = displayName + getString(R.string.removedChats);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}

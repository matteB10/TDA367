package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.ChatPresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

/**
 * Fragment for displaying active chats of the current user.
 *
 */

public class ChatFragment extends Fragment implements ChatPresenter.View {
    private ChatPresenter presenter;
    private View view;
    private MessagesRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.messages_fragment, container, false);
        this.presenter = new ChatPresenter(this);

        return view;

    }
    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of MessageFragment");
        super.onResume();
        if(this.presenter!=null){
            setupList(presenter);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    /**
     * Instantiates the recyclerview which displays the chat_listitems.
     */
    private void setupList(ChatPresenter chatPresenter) {
        RecyclerView recyclerView = view.findViewById(R.id.messages_recyclerview);
        adapter = new MessagesRecyclerViewAdapter(chatPresenter);
        recyclerView.setAdapter(adapter);
        int spanCount = 1;
        int spacing = 10;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));


    }

    /**
     * Displays a loadingscreen in form of a progressbar if needed.
     */
    @Override
    public void showLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.VISIBLE);

    }

    /**
     * Displays chats in the form of list items when desired.
     *
     */
    @Override
    public void showThumbnails(ChatPresenter chatPresenter) {
        setupList(chatPresenter);

    }

    /**
     * Hides loadingscreen.
     */
    @Override
    public void hideLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.GONE);

    }

    /**
     * Displays the messages screen of the chosen chat.
     *
     */

    @Override
    public void showMessagesScreen(String chatID) {
        Intent intent = new Intent(getContext(), MessagesActivity.class);
        intent.putExtra("chatID",chatID);
        startActivity(intent);

    }

    /**
     * Displays the chats of the current user.
     *
     */

    @Override
    public void showUserChats(ChatPresenter chatPresenter) {
        showLoadingScreen();
        showThumbnails(chatPresenter);
        hideLoadingScreen();

    }
}

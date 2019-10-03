package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.MessagesPresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

public class MessagesFragment extends Fragment implements MessagesPresenter.View {
    private MessagesPresenter presenter;
    private View view;
    private MessagesRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.messages_fragment, container, false);
        this.presenter = new MessagesPresenter(this);

        return view;

    }


    private void setupList(MessagesPresenter messagesPresenter) {
        RecyclerView recyclerView = view.findViewById(R.id.messages_recyclerview);
        adapter = new MessagesRecyclerViewAdapter(messagesPresenter);
        recyclerView.setAdapter(adapter);
        int spanCount = 1;
        int spacing = 10;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));


    }

    @Override
    public void showLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void showThumbnails(MessagesPresenter messagesPresenter) {
        setupList(messagesPresenter);

    }

    @Override
    public void hideLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void showDetailsScreen(String chatID) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("chatID",chatID);
        startActivity(intent);

    }

    @Override
    public void isLoggedIn(MessagesPresenter messagesPresenter) {
        showLoadingScreen();
        showThumbnails(messagesPresenter);
        hideLoadingScreen();

    }
}

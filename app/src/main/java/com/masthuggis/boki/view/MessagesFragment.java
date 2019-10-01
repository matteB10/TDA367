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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.presenter = new MessagesPresenter(this);
        this.view = inflater.inflate(R.layout.messages_fragment, container, false);
        showLoadingScreen();
        showThumbnails();
        hideLoadingScreen();
        return view;

    }



    private void setupList() {
        RecyclerView recyclerView = view.findViewById(R.id.messages_recyclerview);
        MessagesRecyclerViewAdapter adapter = new MessagesRecyclerViewAdapter(presenter);
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
    public void showThumbnails() {
        setupList();

    }

    @Override
    public void hideLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void showDetailsScreen(String id) {
        Intent intent = new Intent(getContext(),ChatActivity.class);
        intent.putExtra("userID",id);
        startActivity(intent);

    }
}

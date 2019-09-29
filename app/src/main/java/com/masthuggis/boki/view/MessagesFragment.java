package com.masthuggis.boki.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.MessagesPresenter;
import com.masthuggis.boki.presenter.ProductsRecyclerViewAdapter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

public class MessagesFragment extends Fragment {
    private MessagesPresenter presenter;
    private View view;
    private int spanCount = 1;
    private int spacing = 40;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.messages_fragment, container, false);
        this.presenter = new MessagesPresenter();
        setupList();
        return view;

    }


    private void setupList() {
        RecyclerView recyclerView = view.findViewById(R.id.messages_recyclerview);
        MessagesRecyclerViewAdapter adapter = new MessagesRecyclerViewAdapter(presenter);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));
    }



}

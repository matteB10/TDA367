package com.masthuggis.boki.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.ProductsRecyclerViewAdapter;
import com.masthuggis.boki.presenter.ProfilePresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;
import com.masthuggis.boki.utils.HeaderDecoration;

public class ProfileFragment extends Fragment implements ProfilePresenter.View {
    private ProfilePresenter presenter = new ProfilePresenter(this);
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment,container,false);
        setupList(v);
        return v;
    }

    private void setupList(View v) {
        recyclerView = v.findViewById(R.id.profileRecyclerView);
        setupAdapter();
        setupListLayout();
        setupListHeader();
    }

    private void setupAdapter() {
        ProductsRecyclerViewAdapter adapter = new ProductsRecyclerViewAdapter(getContext(), presenter);
        recyclerView.setAdapter(adapter);
    }

    private void setupListLayout() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
    }

    private void setupListHeader() {
        View headerView = createHeader("Mina böcker");
        HeaderDecoration headerDecoration = new HeaderDecoration(headerView, false, 0, 0, 2);
        recyclerView.addItemDecoration(headerDecoration);
    }

    private View createHeader(String title) {
        View header = getLayoutInflater().inflate(R.layout.recycler_header, null);
        TextView titleTextView = header.findViewById(R.id.recyclerHeaderTitle);
        titleTextView.setText(title);
        return header;
    }

    @Override
    public void setIsUserLoggedIn(boolean isUserLoggedIn) {
        if (isUserLoggedIn) {
            // TODO: Display the users books on sale
        } else {
            // TODO: ask user to log in? What actions should be taken?
        }
    }

    @Override
    public void updateItemsOnSale(ThumbnailView items) {
        // TODO: update list
    }
}

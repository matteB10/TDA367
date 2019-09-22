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

public class ProfileFragment extends Fragment implements ProfilePresenter.View {
    private ProfilePresenter presenter = new ProfilePresenter(this);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment,container,false);
        TextView headerTextView = v.findViewById(R.id.profileHeaderTextView);
        headerTextView.setText("It's working!");
        return v;
    }

    private void setupList(View v) {
        /*
        RecyclerView recyclerView = v.findViewById(R.id.advertsRecyclerView);
        ProductsRecyclerViewAdapter adapter = new ProductsRecyclerViewAdapter(getContext(), presenter);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
        */
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

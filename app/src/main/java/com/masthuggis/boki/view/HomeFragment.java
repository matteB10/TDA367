package com.masthuggis.boki.view;

import android.content.Intent;
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
import com.masthuggis.boki.presenter.HomePresenter;
import com.masthuggis.boki.presenter.ProductsRecyclerViewAdapter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

/**
 * Home page displaying all the adverts that have been published to the market.
 * Will also include filter and sort buttons in the future.
 */
public class HomeFragment extends Fragment implements HomePresenter.View {

    private HomePresenter presenter;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.home_fragment, container, false);
        this.presenter = new HomePresenter(this);
        return view;
    }

    private void setupList() {
        RecyclerView recyclerView = view.findViewById(R.id.advertsRecyclerView);
        ProductsRecyclerViewAdapter adapter = new ProductsRecyclerViewAdapter(getContext(), presenter);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
    }

    @Override
    public void hideLoadingScreen() {
        TextView loadingTextView = view.findViewById(R.id.homeLoadingTextView);
        loadingTextView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingScreen() {
        TextView loadingTextView = view.findViewById(R.id.homeLoadingTextView);
        loadingTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDetailsScreen(String id) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("advertID", id);
        startActivity(intent);
    }


    @Override
    public void showThumbnails() {
        setupList();
    }


}

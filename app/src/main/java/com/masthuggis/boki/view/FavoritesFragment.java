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
import com.masthuggis.boki.presenter.AdvertsPresenterView;
import com.masthuggis.boki.presenter.FavouritesPresenter;
import com.masthuggis.boki.presenter.IProductsPresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

public class FavoritesFragment extends Fragment implements AdvertsPresenterView {

    private FavouritesPresenter presenter;
    private ProductsRecyclerViewAdapter recyclerViewAdapter;
    private View view;
    ProgressBar progressBar;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.favorites_fragment, container, false);
        initAssets();
        initPresenter();
        setupList();
        return view;
    }

    private void initPresenter() {
        this.presenter = new FavouritesPresenter(this);
    }

    private void initAssets() {
        this.progressBar = view.findViewById(R.id.loadingProgressBar);
        this.recyclerView = view.findViewById(R.id.favouritesRecyclerView);
    }

    private void setupList() {
        recyclerViewAdapter = new ProductsRecyclerViewAdapter(getContext(), presenter);
        recyclerView.setAdapter(recyclerViewAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
    }

    @Override
    public void showLoadingScreen() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoadingScreen() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateThumbnails() {
        if (recyclerViewAdapter == null) {
            setupList();
        } else {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showNoThumbnailsAvailableScreen() {
        // TODO: implement
    }

    @Override
    public void hideNoThumbnailsAvailableScreen() {
        // TODO: implement
    }

    @Override
    public void showDetailsScreen(String id) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("advertID", id);
        startActivity(intent);
    }
}

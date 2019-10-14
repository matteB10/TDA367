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
import com.masthuggis.boki.presenter.AdvertsPresenterView;
import com.masthuggis.boki.presenter.FavouritesPresenter;
import com.masthuggis.boki.presenter.IProductsPresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

public class FavoritesFragment extends Fragment implements AdvertsPresenterView {

    private FavouritesPresenter presenter;
    private ProductsRecyclerViewAdapter recyclerViewAdapter;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.favorites_fragment, container, false);
        initPresenter();
        setupList();
        return view;
    }

    private void initPresenter() {
        this.presenter = new FavouritesPresenter(this);
    }

    private void setupList() {
        RecyclerView recyclerView = view.findViewById(R.id.favouritesRecyclerView);
        recyclerViewAdapter = new ProductsRecyclerViewAdapter(getContext(), presenter); //TODO change this
        recyclerView.setAdapter(recyclerViewAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
    }

    @Override
    public void showLoadingScreen() {

    }

    @Override
    public void updateThumbnails() {

    }

    @Override
    public void hideLoadingScreen() {

    }

    @Override
    public void showDetailsScreen(String id) {

    }
}

package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.FavoritesPresenter;
import com.masthuggis.boki.utils.ViewCreator;
/**
 * View class for showing the favorites part of the graphical interface.
 * Used by MainActivity.
 * Written by masthuggis
 */
public class FavoritesFragment extends ListView {

    private FavoritesPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.presenter = new FavoritesPresenter(this, DependencyInjector.injectDataModel());
        View v = super.onCreateView(inflater, container, savedInstanceState);
        presenter.updateData();
        return v;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ProductsRecyclerViewAdapter(getContext(), presenter);
    }

    @Override
    protected View onCreateHeaderLayout() {
        return ViewCreator.createHeader(getActivity(), getString(R.string.yourFavorites));
    }

    @Override
    protected View onCreateNoResultsFoundLayout() {
        return ViewCreator.createSimpleText(getActivity(), getString(R.string.noFavoritesFound));
    }

    @Override
    public void showDetailsScreen(String id) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(getString(R.string.keyForAdvert), id);
        intent.putExtra("fromFavourites", true);
        startActivity(intent);
    }
}

package com.masthuggis.boki.view;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.AdvertsPresenter;
import com.masthuggis.boki.presenter.AdvertsPresenterView;
import com.masthuggis.boki.presenter.FavouritesPresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;
import com.masthuggis.boki.utils.ViewCreator;

public class FavoritesFragment extends AdvertsView implements AdvertsPresenterView {

    private FavouritesPresenter presenter;

    @Override
    protected AdvertsPresenter getPresenter() {
        if (presenter == null) {
            presenter = new FavouritesPresenter(this, DependencyInjector.injectDataModel());
        }
        return presenter;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ProductsRecyclerViewAdapter(getContext(), presenter);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), 2);
    }

    @Override
    protected RecyclerView.ItemDecoration getSpacingDecorator() {
        return new GridSpacingItemDecoration(2, 40, true);
    }

    @Nullable
    @Override
    protected PullToRefreshCallback optionalPullToRefreshHandler() {
        return null;
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
        intent.putExtra("advertID", id);
        intent.putExtra("fromFavourites", true);
        startActivity(intent);
    }
}

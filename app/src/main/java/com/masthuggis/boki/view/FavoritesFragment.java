package com.masthuggis.boki.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.AdvertsPresenter;
import com.masthuggis.boki.presenter.AdvertsPresenterView;
import com.masthuggis.boki.presenter.FavouritesPresenter;
import com.masthuggis.boki.utils.ViewCreator;

public class FavoritesFragment extends AdvertsView implements AdvertsPresenterView {

    private FavouritesPresenter presenter;

    @Override
    protected AdvertsPresenter getPresenter() {
        if (presenter == null) {
            this.presenter = new FavouritesPresenter(this, DependencyInjector.injectDataModel());
        }
        return presenter;
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
}

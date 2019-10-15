package com.masthuggis.boki.view;

import android.view.View;
import android.widget.TextView;

import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.AdvertsPresenter;
import com.masthuggis.boki.presenter.AdvertsPresenterView;
import com.masthuggis.boki.presenter.FavouritesPresenter;

public class FavoritesFragment extends AdvertsView implements AdvertsPresenterView {

    private FavouritesPresenter presenter;

    @Override
    protected AdvertsPresenter getPresenter() {
        if (presenter == null) {
            this.presenter = new FavouritesPresenter(this, DependencyInjector.injectDataModel());
        }
        return presenter;
    }

    @Override
    protected View onCreateHeaderLayout() {
        // TODO: implement
        return new TextView(getActivity());
    }

    @Override
    protected View onCreateNoResultsFoundLayout() {
        // TODO: implement
        return new TextView(getActivity());
    }
}

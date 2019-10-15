package com.masthuggis.boki.view;

import android.view.View;
import android.widget.TextView;

import com.masthuggis.boki.R;
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
        TextView textView = new TextView(getActivity());
        textView.setText(getString(R.string.noFavoritesFound));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextAppearance(android.R.style.TextAppearance_Medium);
        return textView;
    }
}

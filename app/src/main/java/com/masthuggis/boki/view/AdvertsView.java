package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.AdvertsPresenter;
import com.masthuggis.boki.presenter.AdvertsPresenterView;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

/**
 * Abstract class to be used by views wanting to display a list of adverts.
 */
abstract class AdvertsView extends Fragment implements AdvertsPresenterView {
    protected AdvertsPresenter presenter;
    private View view;
    private ProductsRecyclerViewAdapter recyclerViewAdapter;
    private LinearLayout noAdvertsFoundContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.adverts_view, container, false);
        this.presenter = getPresenter();
        setupHeader();
        setupNoResultsFoundView();
        this.presenter.initPresenter();
        return view;
    }

    /**
     * Setups the layout to be displayed when no results are found. The default is for this view
     * to be hidden. The view will be viewable if the presenter asks it to showed. The layout to
     * be used is implemented in the subclass.
     */
    private void setupNoResultsFoundView() {
        View noResultsFound = onCreateNoResultsFoundLayout();
        if (noResultsFound != null) {
            noAdvertsFoundContainer = view.findViewById(R.id.advertsViewNoAdvertsFound);
            noAdvertsFoundContainer.addView(noResultsFound);
            noAdvertsFoundContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Setups the layout to be used on top of the list of adverts. It could be viewed as a header
     * of the screen.
     */
    private void setupHeader() {
        View header = onCreateHeaderLayout();
        if (header != null) {
            LinearLayout headerContainer = view.findViewById(R.id.advertsViewHeader);
            headerContainer.addView(header);
        }
    }

    /**
     * Setups the recyclerviews adapter, layout and spacing
     */
    private void setupList() {
        RecyclerView recyclerView = view.findViewById(R.id.advertsViewRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerViewAdapter = new ProductsRecyclerViewAdapter(getContext(), presenter);
        recyclerView.setAdapter(recyclerViewAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
    }

    /**
     * Notifies the presenter that the view has been destroyed. Ideally used by the presenter
     * to prevent memory leaks.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.viewIsBeingDestroyed();
    }

    /**
     * Asks the concrete implementations to provide a layout to be used above the adverts list,
     * acting as a header.
     * @return
     */
    protected abstract View onCreateHeaderLayout();

    /**
     * Asks the concrete implementation to provide a layout for when no adverts are found.
     * @return
     */
    protected abstract View onCreateNoResultsFoundLayout();

    /**
     * Asks the concrete implementation to provide the presenter to be used.
     * @return
     */
    protected abstract AdvertsPresenter getPresenter();

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
        noAdvertsFoundContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoThumbnailsAvailableScreen() {
        noAdvertsFoundContainer.setVisibility(View.GONE);
    }

    @Override
    public void showDetailsScreen(String id) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("advertID", id);
        startActivity(intent);
    }

    @Override
    public void showLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.advertsViewProgressbar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.advertsViewProgressbar);
        progressBar.setVisibility(View.GONE);
    }
}

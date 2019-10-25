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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.ListPresenterView;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

import java.util.List;
import java.util.Optional;

/**
 * Abstract class to be used by views wanting to display a list of content.
 * Used by ChatFragment, HomeFragment, ProfielFragment and FavoritesFragment.
 * Written by masthuggis
 */
public abstract class ListView extends Fragment implements ListPresenterView {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private Optional<RecyclerView.LayoutManager> layoutManager = Optional.empty();
    private Optional<List<RecyclerView.ItemDecoration>> itemDecorations = Optional.empty();
    private LinearLayout noAdvertsFoundContainer;
    private boolean pullToRefreshIsActivated = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.adverts_view, container, false);
        setupHeader();
        setupNoResultsFoundView();
        setupPullToRefresh();
        return view;
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
     * Setups the layout to be displayed when no results are found. The default is for this view
     * to be hidden. The view will be viewable if the presenter asks it to showed. The layout to
     * be used is implemented in the subclass.
     */
    private void setupNoResultsFoundView() {
        View noResultsFound = onCreateNoResultsFoundLayout();
        noAdvertsFoundContainer = view.findViewById(R.id.advertsViewNoAdvertsFound);
        noAdvertsFoundContainer.addView(noResultsFound);
        noAdvertsFoundContainer.setVisibility(View.GONE);
    }

    /**
     * By default the pullToRefresh is not activated.
     */
    private void setupPullToRefresh() {
        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.pullToRefresh);
        swipeRefresh.setEnabled(false);
    }

    /**
     * Setups the recyclerviews adapter, layout and spacing. If values have been provided by the
     * concrete implementation it will use that, else default values will be used.
     */
    private void setupList() {
        recyclerView = view.findViewById(R.id.advertsViewRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerViewAdapter = getAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager.orElse(new GridLayoutManager(getContext(), 2)));
        if (itemDecorations.isPresent()) {
            itemDecorations.get().forEach(item -> recyclerView.addItemDecoration(item));
        } else {
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
        }
    }

    /**
     * Asks the concrete implementations to provide a layout to be used above the adverts list,
     * acting as a header.
     *
     * @return
     */
    protected abstract View onCreateHeaderLayout();

    /**
     * Asks the concrete implementation to provide a layout for when no adverts are found.
     *
     * @return
     */
    protected abstract View onCreateNoResultsFoundLayout();

    protected abstract RecyclerView.Adapter getAdapter();

    void setItemDecorations(List<RecyclerView.ItemDecoration> itemDecorations) {
        this.itemDecorations = Optional.of(itemDecorations);
    }

    void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = Optional.ofNullable(layoutManager);
    }

    /**
     * Gives the concrete implementation the option to provide an action, and therefor activate
     * the pull-to-refresh behavior. If this method is not called pull-to-refresh will not be
     * activated.
     *
     * @return
     */
    void setAndActivatePullToRefreshHandler(PullToRefreshCallback callback) {
        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.pullToRefresh);
        swipeRefresh.setEnabled(true);
        pullToRefreshIsActivated = true;
        swipeRefresh.setOnRefreshListener(() -> {
            callback.onCallback();
            swipeRefresh.setRefreshing(false);
        });
    }

    /**
     * Updates the data that is displayed in the recyclerView. Will be called when for example
     * the sorting changes or a search is performed.
     */
    @Override
    public void updateThumbnails() {
        if (recyclerViewAdapter == null) {
            setupList();
        } else {
            recyclerViewAdapter.notifyDataSetChanged();
            //  recyclerView.setVisibillity(View.VISIBLE);
        }
    }

    /**
     * Shows a message explaining that no data is available with the layout provided by the
     * concrete implementations. Also hides the recycler (by hiding the containing refreshlayout)
     */
    @Override
    public void showNoThumbnailsAvailableScreen() {
        noAdvertsFoundContainer.setVisibility(View.VISIBLE);
        if (recyclerView != null) {
            recyclerView.setVisibility(View.INVISIBLE);
        }
        if (pullToRefreshIsActivated) {
            view.findViewById(R.id.pullToRefresh).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Hides the message explaining that no data is available. For example called whenever there
     * is data to show.
     */
    @Override
    public void hideNoThumbnailsAvailableScreen() {
        noAdvertsFoundContainer.setVisibility(View.GONE);
        if (pullToRefreshIsActivated) {
            view.findViewById(R.id.pullToRefresh).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Opens a details view showing more information about the advert that was pressed.
     *
     * @param id
     */
    @Override
    public void showDetailsScreen(String id) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(getString(R.string.keyForAdvert), id);
        startActivity(intent);
    }

    /**
     * Shows a progressbar to indicate that content is loading. For example used when a network
     * request is made.
     */
    @Override
    public void showLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.advertsViewProgressbar);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the progressbar that indicates that content is loading.
     */
    @Override
    public void hideLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.advertsViewProgressbar);
        progressBar.setVisibility(View.GONE);
    }
}
